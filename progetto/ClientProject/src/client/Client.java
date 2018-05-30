package client;

import interfaces.ClientInterface;
import interfaces.ServerInterface;
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
    private String plainPassword;
    private ClientInterface skeleton;//client's stub
    private ServerInterface server_stub;//broker's stub
    private long cookie;
    private String bp_key; //broker's public key
    private String my_private_key;
    private static final int port=8000;

    // ************************************************************************************************************
    //CONSTRUCTORS

    //User's constructor

    public Client(String username, String light_password, ClientInterface skeleton, String bp_key, String my_private_key)
            throws RemoteException
    {
        if(username==null||light_password==null||skeleton==null||bp_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.plainPassword=light_password;
        this.skeleton=skeleton;
        this.bp_key=bp_key;
        this.my_private_key=my_private_key;

    }

    //Anonymous user's constructor

    public Client(String username, ClientInterface skeleton, String bp_key, String my_private_key)
            throws RemoteException
    {
        if(username==null||skeleton==null||bp_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.skeleton=skeleton;
        this.bp_key=bp_key;
        this.my_private_key=my_private_key;
    }


    // *************************************************************************************************************
    //API

    //TODO aggiungere i metodi elencati nel file che specifica le API del client

    //Registration on a server
    public void Register() throws RemoteException,NotBoundException
    {
        Registry r = LocateRegistry.getRegistry(port);
        this.server_stub = (ServerInterface) r.lookup("REG");
        ResponseCode response =server_stub.register(this.skeleton,username,plainPassword);
        switch (response.getCodice()) {
            case R100:
                this.cookie=Long.valueOf(response.getMessaggioInfo());
                System.out.println("Sono stato registrato!");
                break;
            case R600:
                System.err.println(response.getCodice() + response.getMessaggioInfo());
                break;

                default:

        }
    }

    //Connection on a server
    public void Connect() throws RemoteException,NotBoundException
    {
        Registry r = LocateRegistry.getRegistry(port);
        this.server_stub= (ServerInterface) r.lookup("REG");
        ResponseCode response= server_stub.connect(this.skeleton,username,plainPassword);
        switch (response.getCodice()) {
            case R100:
                this.cookie=Long.valueOf(response.getMessaggioInfo());
                System.out.println("Connesso con successo!");
                break;
            case R600:
                System.err.println(response.getCodice() + response.getMessaggioInfo());
                break;

            default:
        }
    }

    //Anonymous connection on a server
    public void AnonymousConnect() throws RemoteException,NotBoundException
    {
        Registry r = LocateRegistry.getRegistry(port);
        this.server_stub= (ServerInterface) r.lookup("REG");

        //TODO anonymous server's connect
        ResponseCode response= server_stub.connect(this.skeleton,null,null);
        switch (response.getCodice()) {
            case R100:
                this.cookie=Long.valueOf(response.getMessaggioInfo());//per la connect anonima il cookie non viene settato
                System.out.println("Connesso anonimamente con successo!");
                break;
            case R600:
                System.err.println(response.getCodice() + response.getMessaggioInfo());
                break;

            default:
        }
    }

    public void Subscribe(Topic topic)
    {
    }


    // *************************************************************************************************************
    //REMOTE METHOD

    @Override
    public ResponseCode notify(Message m) /*throws RemoteException*/ {
        ResponseCode rc;
        if(m==null) {
             rc=new ResponseCode(ResponseCode.Codici.R500, ResponseCode.TipoClasse.CLIENT,
                    "(-) NOT OK Il server ha ricevuto un messaggio vuoto");
            return rc;
        }
         rc=new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.CLIENT,
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

}
