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

import interfaces.ClientInterface;
import interfaces.ServerInterface;
import utility.Message;
import utility.ResponseCode;

import java.rmi.RemoteException;

import static utility.ResponseCode.Codici.R220;


public class Client extends AnonymousClient {
    static final private String className = "CLIENT";

    /******************/
    /* client fields */
    private String username;
    private String plainPassword;
    private ClientInterface skeleton;//my stub
    private String cookie;
    private String myPrivateKey;
    private String myPublicKey;
    private boolean pedantic  = true;

    private String[] topicsSubscribed;                         //topic a cui si è iscritti

    /******************/
    /* server fields */
    private String serverName;                      //the name for the remote reference to look up
    private String brokerPublicKey;                 //broker's public key
    private ServerInterface server_stub;            //broker's stub
    private String[] topicOnServer;                 //topic che gestisce il server

    /* remote registry fields */
    private String registryHost;                    //host for the remote registry
    private int registryPort = 1099;                //port on which the registry accepts requests




    // ************************************************************************************************************
    //CONSTRUCTORS

    /**
     * Client's constructor
     * @param username          identificativo client
     * @param plainPassword     password in chiaro
     * @param my_private_key    la mia chiave privata
     * @param my_public_key     la mia chiave pubblica
     */
    public Client(String username, String plainPassword, String my_public_key, String my_private_key ) throws RemoteException
    {
        super(username,my_public_key,my_private_key);
        if(plainPassword==null)
            throw new NullPointerException();
        this.plainPassword=plainPassword;
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
            ResponseCode responseCode = server_stub.register(this.username, this.plainPassword, this.skeleton, this.myPublicKey,"emailTest@qualcosa.org");
            return registered(responseCode);
        }catch (RemoteException e){
            errorStamp(e, "Unable to reach the server.");
            return false;
        }
    }



    /**
     * Si chiede al server di recuperare le informazioni legate al nostro account
     * @return TRUE se andata a buon fine,FALSE altrimenti
     */
    @Override
    public boolean retrieveAccount(){
        if(connected()) {
            try {
                ResponseCode response = server_stub.retrieveAccount(username, plainPassword, skeleton);
                if (response.getCodice() == R220) {
                    infoStamp("Account successfully recovered.");
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
            Message msg = createMessage(topic, title, text);
            if(msg == null)     //errore durante la creazione di un messaggio
                return false;

            try {
                ResponseCode response = null;
                //todo aggiungere un codice di risposta alla publish
                /*code = */server_stub.publish(this.cookie, msg);
                if(response.IsOK())
                {
                    infoStamp("Message published.");
                    return true;
                }
                else
                    errorStamp(response, "Error while publishing the message.");
            }catch (RemoteException e){
                errorStamp(e, "Unable to reach the server.");
                return false;
            }
        }
        errorStamp("Not connected to any server.");
        return false;
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







}
