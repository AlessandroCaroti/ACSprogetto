package client;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;
import utility.Message;
import utility.ResponseCode;
import utility.Topic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client  extends UnicastRemoteObject implements ClientInterface {

    //Variabili di istanza

    private String username;
    private String light_password;
    private ClientInterface stub;
    private ServerInterface skeleton;//broker stub
    private long cookie;
    private String bp_key; //broker public key
    private String my_private_key;
    private static long last_cookie;
    private static final int port=8000;

    // ************************************************************************************************************
    //Costruttore

    public Client(String username, String light_password, ClientInterface stub, ServerInterface skeleton, String bp_key, String my_private_key)
            throws RemoteException
    {
        if(username==null||light_password==null||stub==null||skeleton==null||bp_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.light_password=light_password;
        this.stub=stub;
        this.skeleton=skeleton;
        this.cookie=last_cookie++;
        this.bp_key=bp_key;
        this.my_private_key=my_private_key;
    }

    // *************************************************************************************************************
    //API

    /*TODO
    aggiungere i metodi elencari nel file che specifica le API del client
     */
    // Registrazione su un server
    public void Register(ServerInterface skeleton ,String Username,String Password) throws RemoteException,NotBoundException
    {
        Registry r = LocateRegistry.getRegistry(port);
        skeleton= (ServerInterface) r.lookup("REG");
        //r.rebind("ClientReg",stub);
        ResponseCode response =skeleton.register(Username,Password);
    }

    public void Connect(ServerInterface skeleton,String Username,String Password) throws RemoteException,NotBoundException
    {
        Registry r = LocateRegistry.getRegistry(port);
        skeleton= (ServerInterface) r.lookup("REG");
        ResponseCode response= skeleton.connect(Username,Password);
        if(!response.IsOK())
        {

        }

    }


    // *************************************************************************************************************
    //METODI REMOTI

    @Override
    public ResponseCode notify(Message m) /*throws RemoteException*/ {
        ResponseCode rc;
        if(m==null) {
             rc=new ResponseCode(ResponseCode.Codici.R500, ResponseCode.TipoClasse.CLIENT,
                    "NOT OK Il server ha ricevuto un messaggio vuoto");
            return rc;
        }
         rc=new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.CLIENT,
                "OK il server ha ricevuto il messaggio");
        return rc;
    }

    /* is throw remoteException necessary?*/
    @Override
    public boolean isAlive()/* throws RemoteException*/ {
        return true;
    }

    // *************************************************************************************************************
    //METODI PRIVATI

}
