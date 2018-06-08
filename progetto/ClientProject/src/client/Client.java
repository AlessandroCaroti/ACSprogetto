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

import static utility.ResponseCode.*;



public class Client  implements ClientInterface {

    //Instance variables

    private String username;
    private String plainPassword;
    private ClientInterface skeleton;//my stub
    private String cookie;
    private String myPrivateKey;
    private String myPublicKey;


    private String broker;
    private ServerInterface server_stub;//broker's stub
    public static final int defaultPort=1099;
    private int port;
    private String brokerPublicKey; //broker's public key

    // ************************************************************************************************************
    //CONSTRUCTORS

    //Client's constructor
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

    //Anonymous user's constructor

    /**
     * @param username il mio username
     * @param my_private_key la mia chiave privata
     * @param my_public_key la mia chiave pubblica
     */
    public Client(String username, String my_public_key, String my_private_key)throws RemoteException
    {
        this(username,"",my_public_key,my_private_key);
    }





    // *************************************************************************************************************
    //API

    //TODO aggiungere i metodi elencati nel file che specifica le API del



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

    //Registration on the server
    /**
     *Il client si registra sul server su cui si era connesso con il metodo connect() e viene settato il cookie
     * @return true se registrazione andata a buon fine, false altrimenti
     */
    public boolean register() {
        try {
            ResponseCode responseCode = server_stub.register(this.username, this.plainPassword, this.skeleton, this.myPublicKey);
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
     * Si connette al server specificato dalla stringa broker e dalla porta port facendo il lookup
     * sul registry dell'host
     * @param broker il broker su cui connettersi
     * @param port se null viene usata defaultport
     * @throws NullPointerException se broker == null
     * @return true se andata a buon fine,false altrimenti
     */
    public boolean connect(String broker,Integer port) throws NullPointerException
    {
        /*init*/
        if(broker==null){
            throw new NullPointerException("broker string == null");
        }
        if(port==null){
            this.port=defaultPort;
        }else{
            this.port=port;
        }
        this.broker=broker;
        try {
            Registry r = LocateRegistry.getRegistry(this.broker, this.port);
            this.server_stub = (ServerInterface) r.lookup("ServerInterface");
            ResponseCode response = server_stub.connect(this.skeleton, myPublicKey);

            switch (response.getCodice()) {
                case R210:
                    this.brokerPublicKey = response.getMessaggioInfo();
                    return true;
                default:
                    System.out.println(response.getCodice()+":"+response.getMessaggioInfo()+"  FROM:"+response.getClasseGeneratrice());
                    return false;
            }
        }catch (RemoteException |NotBoundException exc){
            System.err.println(exc.getClass().getSimpleName());
            exc.printStackTrace();
            return false;
        }
    }


    public void subscribe(Topic topic)
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
            this.broker=null;
            return uscita;

        }catch(RemoteException exc){
            System.err.println(exc.getClass().getSimpleName());
            return false;
        }

    }


    public boolean retrieveAccount(){
        try{
            ResponseCode response=server_stub.retrieveAccount(username,plainPassword,skeleton,cookie);
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

}
