/**
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

**/
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


public class Client  implements ClientInterface {

    /******************/
    /* client fields */
    private String username;
    private String plainPassword;
    private ClientInterface skeleton;//my stub
    private String cookie;
    private String myPrivateKey;
    private String myPublicKey;
    private boolean pedantic = true;

    /******************/
    /* server fields */
    private String serverName;                      //the name for the remote reference to look up
    private String brokerPublicKey;                 //broker's public key
    private ServerInterface server_stub;            //broker's stub

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
        if(username==null||plainPassword==null||my_public_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.plainPassword=plainPassword;
        this.myPublicKey=my_public_key;
        this.myPrivateKey=my_private_key;
        this.skeleton=(ClientInterface) UnicastRemoteObject.exportObject(this,0);

    }


    /**
     * Anonymous user's constructor
     * @param username          il mio username
     * @param my_private_key    la mia chiave privata
     * @param my_public_key     la mia chiave pubblica
     */
    public Client(String username, String my_public_key, String my_private_key)throws RemoteException
    {
        this(username,"",my_public_key,my_private_key);
    }





    // *************************************************************************************************************
    //API


    /**
     *Il client si registra sul server su cui si era connesso con il metodo connect() e viene settato il cookie
     * @return true se registrazione andata a buon fine, false altrimenti
     */
    public boolean register() {
        try {
            ResponseCode responseCode = server_stub.register(this.username, this.plainPassword, this.skeleton, this.myPublicKey,"emailTest@qualcosa.org");
            if (responseCode.getCodice().equals(Codici.R100)) {
                this.cookie = responseCode.getMessaggioInfo();
                return true;
            }else{
                if (responseCode.getCodice().equals(Codici.R610)) {
                    System.out.println(responseCode.getMessaggioInfo());
                } else {
                    System.out.println(responseCode.getCodice()+":"+responseCode.getMessaggioInfo()+"  FROM:"+responseCode.getClasseGeneratrice());
                }
                return false;
            }
        }catch (RemoteException e){
            System.err.println("Remote exception:"+e.getClass().getSimpleName());
            return false;
        }
    }

    public boolean anonymousRegister(){
        try {
            ResponseCode responseCode = server_stub.anonymousRegister(this.skeleton, this.myPublicKey);
            if (responseCode.getCodice().equals(Codici.R100)) {
                this.cookie = responseCode.getMessaggioInfo();
                return true;
            } else {
                if (responseCode.getCodice().equals(Codici.R610)) {
                    System.out.println(responseCode.getMessaggioInfo());

                } else {
                    System.out.println(responseCode.getCodice() + ":" + responseCode.getMessaggioInfo() + "  FROM:" + responseCode.getClasseGeneratrice());
                }
                return false;
            }
        }catch (RemoteException e){
            System.err.println("Remote exception:"+e.getClass().getSimpleName());
            return false;
        }
    }





    /**
     * Si connette al server specificato dalla stringa broker e dalla porta regPort facendo il lookup
     * sul registry dell'host
     * @param regHost l'indirizzo della macchina su cui risiede il registry
     * @param regPort porta su cui connettersi al registro
     * @return true se andata a buon fine,false altrimenti
     */

    public ServerInterface connect(String regHost, String server, Integer regPort)
    {
        try {
            Registry r = LocateRegistry.getRegistry(regHost, regPort);
            ServerInterface server_stub = (ServerInterface) r.lookup(server);
            ResponseCode rc = server_stub.connect();
            if(rc.IsOK())
                this.brokerPublicKey = rc.getMessaggioInfo();
            return server_stub;
        }catch (RemoteException |NotBoundException exc){
            return null;
        }
    }


    public void subscribe(String topic)
    {




    }

    public boolean disconnect(){
        try {
            boolean uscita=false;
            ResponseCode response=server_stub.disconnect(cookie);
            switch(response.getCodice())
            {
                case R200:
                    uscita=true;
                    System.out.println(response.getCodice()+":"+response.getClasseGeneratrice()+":"+response.getMessaggioInfo());
                    break;
                default:
                    System.err.println(response.getCodice()+":"+response.getClasseGeneratrice()+":"+response.getMessaggioInfo());
                    uscita=false;
                    break;
            }
            this.server_stub=null;
            return uscita;

        }catch(RemoteException exc){
            System.err.println(exc.getClass().getSimpleName());
            return false;
        }

    }


    public boolean retrieveAccount(){
        try{
            ResponseCode response=server_stub.retrieveAccount(username,plainPassword,skeleton);
            switch(response.getCodice())
            {
                case R220:
                    System.out.println(response.getCodice()+":"+response.getClasseGeneratrice()+":"+response.getMessaggioInfo());
                    return true;
                default:
                    System.err.println(response.getCodice()+":"+response.getClasseGeneratrice()+":"+response.getMessaggioInfo());
                    return false;
            }
        }catch(RemoteException exc){
            System.err.println(exc.getClass().getSimpleName());
            return false;
        }
    }





    //metodi non ancora utilizzati ma che penso possano servire più tardi
    public void setServerInfo(String regHost, String serverName){
        if(regHost==null || regHost.isEmpty() || serverName==null || serverName.isEmpty()){
            throw new IllegalArgumentException("Invalid argument format of regHost or serverName");
        }
        this.registryHost = regHost;
        this.serverName   = serverName;

    }

    public void setServerInfo(String regHost, int regPort, String serverName) throws IllegalArgumentException{
        if(regHost==null || regHost.isEmpty() || serverName==null || serverName.isEmpty()){
            throw new IllegalArgumentException("Invalid argument format of regHost or serverName");
        }
        if(regPort>1024 && regPort<=65535)  //Se la porta passata è valida impostala come porta del server
            this.registryPort = regPort;
        setServerInfo(regHost, serverName);
        this.server_stub = connect(regHost, serverName, regPort);
        if(server_stub!=null){      //connesione al server avvenuta con successo
            infoStamp("Successful connection to the server.");
        }else {
            infoStamp("Unable to reach the server.");
        }
    }




    // *************************************************************************************************************
    //REMOTE METHOD

    @Override
    public ResponseCode notify(Message m) /*throws RemoteException*/ {
        ResponseCode rc;
        if(m==null) {
             rc=new ResponseCode(Codici.R500, TipoClasse.CLIENT,
                    "(-) NOT OK Il server ha ricevuto un messaggio vuoto");
            return rc;
        }
         rc=new ResponseCode(Codici.R200, TipoClasse.CLIENT,
                "(+) OK il server ha ricevuto il messaggio");
        return rc;
    }

    /* is throw remoteException necessary?*/
    @Override
    public boolean isAlive()/* throws RemoteException*/ {
        return true;
    }




    // *************************************************************************************************************
    //PRIVATE METHOD





































    //METODI UTILIZZATI PER LA GESTIONE DELL'OUTPUT DEL CLIENT

    private void errorStamp(Exception e){
        System.out.flush();
        System.err.println("[CLIENT-ERROR]");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[CLIENT-ERROR]: "      + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void warningStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[CLIENT-WARNING]: "    + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    private void infoStamp(String msg){
        System.out.println("[CLIENT-INFO]: " + msg);
    }

    private void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }
}
