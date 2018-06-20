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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import static utility.ResponseCode.*;
import static utility.ResponseCode.Codici.R220;


public class Client extends AnonymousClient {

    /******************/
    /* client fields */
    private String username;
    private String plainPassword;
    private ClientInterface skeleton;//my stub
    private String cookie;
    private String myPrivateKey;
    private String myPublicKey;
    private boolean anonymous = false;
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
     * @return true se andata a buon fine,false altrimenti
     */
    public boolean retrieveAccount(){
        try{
            ResponseCode response=server_stub.retrieveAccount(username,"",skeleton);
            if(response.getCodice() == R220)
            {
                infoStamp("Account successfully recovered.");
                return true;
            }
            errorStamp(response, "Impossible to retrieve information.");
            return false;
        }catch(RemoteException exc){
            errorStamp(exc, "Unable to reach the server.");
            return false;
        }
    }




    // *************************************************************************************************************
    //PRIVATE METHOD
    /*

     */
    private boolean registered(ResponseCode response){
        if(response == null || !response.getCodice().equals(Codici.R100)) {     //Registrazione fallita
            infoStamp("Server registration failed");
            infoStamp("Server error code: "    + response.getCodice());
            infoStamp("Server error message: " + response.getMessaggioInfo());
            return false;
        }

        //Registrazione avvenuta con successo
        this.cookie = response.getMessaggioInfo();
        infoStamp("Successfully registered on server "+serverName+".");
        return true;
    }




































    //METODI UTILIZZATI PER LA GESTIONE DELL'OUTPUT DEL CLIENT
    protected void errorStamp(Exception e){
        System.out.flush();
        System.err.println("[CLIENT-ERROR]");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    protected void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[CLIENT-ERROR]: "      + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    protected void warningStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[CLIENT-WARNING]: "    + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    protected void infoStamp(String msg){
        System.out.println("[CLIENT-INFO]: " + msg);
    }

    protected void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }
}
