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
    private ClientInterface skeleton;//my stub
    private long cookie;
    private String myPrivateKey;
    private String myPublicKey;


    private String broker;
    private ServerInterface server_stub;//broker's stub
    private static final int defaultPort=8000;
    private int port;
    private String bp_key; //broker's public key

    // ************************************************************************************************************
    //CONSTRUCTORS

    //Client's constructor
    public Client(String username, String plainPassword, ClientInterface skeleton, String my_public_key, String my_private_key )
            throws RemoteException
    {
        if(username==null||plainPassword==null||skeleton==null||my_public_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.plainPassword=plainPassword;
        this.skeleton=skeleton;
        this.myPublicKey=bp_key;
        this.myPrivateKey=my_private_key;

    }

    //Anonymous user's constructor

    /**
     * @param username il mio username
     * @param skeleton il mio stub
     * @param my_private_key la mia chiave privata
     * @param my_public_key la mia chiave pubblica
     * @throws RemoteException
     */
    public Client(String username, ClientInterface skeleton, String my_private_key, String my_public_key)
            throws RemoteException
    {
        if(username==null||skeleton==null||my_public_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.skeleton=skeleton;
        this.myPublicKey=bp_key;
        this.myPrivateKey=my_private_key;
    }


    // *************************************************************************************************************
    //API

    //TODO aggiungere i metodi elencati nel file che specifica le API del client
    //Registration on the server
    /**
     *
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void Register() throws RemoteException,NotBoundException {

    }

    public void AnonymousRegister(){







    }






    /**
     * Si connette al server specificato dalla stringa broker e dalla porta port facendo il lookup
     * sul registry dell'host
     * @param broker il broker su cui connettersi
     * @param port se null viene usata defaultport
     * @throws RemoteException
     * @throws NotBoundException
     * @throws NullPointerException se broker == null
     */
    public void Connect(String broker,Integer port) throws RemoteException,NotBoundException,NullPointerException
    {
        if(broker==null){
            throw new NullPointerException("broker string == null");
        }
        this.broker=broker;
        if(port==null){
            this.port=defaultPort;
        }else{
            this.port=port;
        }

        Registry r = LocateRegistry.getRegistry(this.broker,this.port);
        this.server_stub= (ServerInterface) r.lookup("REG");
        ResponseCode response= server_stub.connect(this.skeleton,username,plainPassword);
        switch (response.getCodice()) {
            case R100:
                System.out.println("Connesso con successo!");
                break;
            case R600:
                System.err.println(response.getCodice() + response.getMessaggioInfo());
                break;

            default:
                //TODO add something here
        }
    }

    //Anonymous connection on a server
   /* public void AnonymousConnect(String broker,Integer port) throws RemoteException,NotBoundException,NullPointerException
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

    }*/

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
