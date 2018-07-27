/*
    This file is part of ACSprogetto.

    ACSprogetto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ACSprogetto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ACSprogetto.  If not, see <http://www.gnu.org/licenses/>.

*/
package client;

import utility.Message;
import utility.ResponseCode;
import java.rmi.RemoteException;
import static utility.ResponseCode.Codici.R220;


public class Client extends AnonymousClient {

    /******************/
    /* client fields */
    private String plainPassword;
    private String email;
    // ************************************************************************************************************
    //CONSTRUCTORS

    /**
     * Client's constructor
     * @param username          identificativo client
     * @param plainPassword     password in chiaro
     * @param my_private_key    la mia chiave privata
     * @param my_public_key     la mia chiave pubblica
     * @param email             la mail associata all'account
     */
    public Client(String username, String plainPassword, String my_public_key, String my_private_key,String email ) throws RemoteException
    {
        super(username,my_public_key,my_private_key);
        if(plainPassword==null||email==null)
            throw new NullPointerException();
        this.plainPassword=plainPassword;
        this.email=email;
        this.className="CLIENT";
    }


    // *************************************************************************************************************
    //API


    /**
     *Il client si registra sul server su cui si era connesso con il metodo connect() e viene settato il cookie
     * @return true se registrazione andata a buon fine, false altrimenti
     */
    @Override
    public boolean register() {
        try {
            ResponseCode responseCode = server_stub.register(this.username, this.plainPassword, this.skeleton, this.myPublicKey,this.email);
            return registered(responseCode);
        }catch (RemoteException e){
            errorStamp(e, "Unable to reach the server.");
            return false;
        }
    }



    /**
     * Si chiede al server di recuperare le informazioni legate al nostro account
     * Viene prima tentato l'accesso tramite (cookie,password) se fallisce tenta con (username,password)
     * @return TRUE se andata a buon fine,FALSE altrimenti
     */
    @Override
    public boolean retrieveAccount(){
        ResponseCode response;
        if(connected()) {
            try {

                if(this.getCookie()!=null) {
                    response = server_stub.retrieveAccountByCookie(this.getCookie(),this.plainPassword,this.skeleton);
                    if (response.getCodice() == R220) {
                        infoStamp("Account successfully recovered.");
                        return true;
                    }else{
                        this.cookie=null;
                        infoStamp("Invalid cookie trying with username and password");
                    }
                }
                response = server_stub.retrieveAccount(username, plainPassword, skeleton);
                if (response.getCodice() == R220 && this.retrieveCookie()) {
                    infoStamp("Account and cookie successfully recovered.");
                    return true;
                }
                errorStamp(response, "Impossible to retrieve information.");
                return false;
            } catch (RemoteException exc) {
                errorStamp(exc, "Unable to reach the server.");
                return false;
            }
        }
        errorStamp("Not connected to any server.");
        return false;
    }


    /**
     * pubblica un messaggio sul server a cui si è connessi
     * @param topic     il topic su cui pubblicare il messsaggio
     * @param title     il titolo del messaggio da inviare
     * @param text      il testo  del messaggio da inviare
     * @return          TRUE se andata a buon fine,FALSE altrimenti
     */
    @Override
    public boolean publish( String topic, String title, String text){
        if(connected()) {
            try {
                Message msg = createMessage(topic, title, text);
                ResponseCode response;
                response=server_stub.publish(this.cookie, msg);
                if(response.IsOK())
                {
                    infoStamp("Message published.");
                    return true;
                }
                else {
                    errorStamp(response, "Error while publishing the message.");
                }
            }catch (Exception e) {
                errorStamp(e);
                return false;
            }
        }else {
            errorStamp("Not connected to any server.");
        }
        return false;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getClassName() {
        return this.className;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // *************************************************************************************************************
    //PRIVATE METHOD

    private Message createMessage(String topic, String title, String text){
        Message msg = null;
        try {
            msg = new Message(title, this.username, text, topic);
        } catch (Exception e) {
            errorStamp("An exception has been thrown during the creation of a message.");
        }
        return msg;
    }


    private boolean retrieveCookie(){

        try {
            if(connected()) {
                ResponseCode response = server_stub.retrieveCookie(this.username, this.plainPassword);
                if (response == null || !response.getCodice().equals(ResponseCode.Codici.R100)) {
                    errorStamp(response, "cookie retrieve failed");
                    return false;
                }
                this.cookie = response.getMessaggioInfo();
                infoStamp("Cookie successfully retrieved.");
                return true;
            }else{
                errorStamp("Not connected to any server.");
                return false;
            }
        }catch (RemoteException e){
            errorStamp(e, "Unable to reach the server.");
            return false;
        }
    }




}
