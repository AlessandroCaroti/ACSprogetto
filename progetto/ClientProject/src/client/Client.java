package client;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;
import utility.GuiInterfaceStream;
import utility.Message;
import utility.ResponseCode;
import utility.Topic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client  extends UnicastRemoteObject implements ClientInterface {

    //Instance variables

    private String username;
    private String light_password;
    private ClientInterface skeleton;//client's stub
    private ServerInterface server_stub;//broker's stub
    private long cookie;
    private String bp_key; //broker's public key
    private String my_private_key;
    private static long last_cookie;
    private static final int port=8000;
    private GuiInterfaceStream gui;

    // ************************************************************************************************************
    //CONSTRUCTORS

    //User's constructor

    public Client(String username, String light_password, ClientInterface skeleton, String bp_key, String my_private_key)
            throws RemoteException
    {
        if(username==null||light_password==null||skeleton==null||bp_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.light_password=light_password;
        this.skeleton=skeleton;
        this.cookie=last_cookie++;
        this.bp_key=bp_key;
        this.my_private_key=my_private_key;
        this.gui=new GuiInterfaceStream(true);

    }

    //Anonymous user's constructor

    public Client(String username, ClientInterface skeleton, String bp_key, String my_private_key)
            throws RemoteException
    {
        if(username==null||skeleton==null||bp_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.skeleton=skeleton;
        this.cookie=last_cookie++;
        this.bp_key=bp_key;
        this.my_private_key=my_private_key;
        this.gui=new GuiInterfaceStream(true);

    }


    // *************************************************************************************************************
    //API

    /*TODO aggiungere i metodi elencari nel file che specifica le API del client
     */
    //Registration on a server
    public void Register() throws RemoteException,NotBoundException
    {
        Registry r = LocateRegistry.getRegistry(port);
        this.server_stub = (ServerInterface) r.lookup("REG");
        ResponseCode response =server_stub.register(this.skeleton,username,light_password);
        if(!response.IsOK())
            gui.stdErrStream.println(response);

    }
    //Connection on a server
    public void Connect() throws RemoteException,NotBoundException
    {
        Registry r = LocateRegistry.getRegistry(port);
        this.server_stub= (ServerInterface) r.lookup("REG");
        ResponseCode response= server_stub.connect(this.skeleton,username,light_password);
        if(!response.IsOK())
            gui.stdErrStream.println(response);
    }


    // *************************************************************************************************************
    //REMOTE METHOD

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
    //PRIVATE METHOD

}
