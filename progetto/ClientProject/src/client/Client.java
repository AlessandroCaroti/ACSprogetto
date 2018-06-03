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
    private static final int defaultPort=8000;
    private int port;
    private String brokerPublicKey; //broker's public key

    // ************************************************************************************************************
    //CONSTRUCTORS

    //Client's constructor
    public Client(String username, String plainPassword, ClientInterface skeleton, String my_public_key, String my_private_key )

    {
        if(username==null||plainPassword==null||skeleton==null||my_public_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.plainPassword=plainPassword;
        this.skeleton=skeleton;
        this.myPublicKey=my_public_key;
        this.myPrivateKey=my_private_key;

    }

    //Anonymous user's constructor

    /**
     * @param username il mio username
     * @param skeleton il mio stub
     * @param my_private_key la mia chiave privata
     * @param my_public_key la mia chiave pubblica
     */
    public Client(String username, ClientInterface skeleton, String my_public_key, String my_private_key)

    {
        this(username,"",skeleton,my_public_key,my_private_key);
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
    private boolean register()  {
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
        }catch (RemoteException exc){
            System.err.println(exc.getClass().getSimpleName());
            return false;
        }
    }

    public boolean anonymousRegister(){
        try {
            ResponseCode responseCode = server_stub.anonymousRegister(this.skeleton,this.myPublicKey);

            if (responseCode.getCodice().equals(Codici.R100)) {
                this.cookie = responseCode.getMessaggioInfo();
                return true;
            }
            else {
                if (responseCode.getCodice().equals(Codici.R610)) {
                    System.out.println(responseCode.getMessaggioInfo());

                } else {
                    System.out.println(responseCode.getCodice()+":"+responseCode.getMessaggioInfo()+"  FROM:"+responseCode.getClasseGeneratrice());
                }
                return false;
            }
        }catch (RemoteException exc){
            System.err.println(exc.getClass().getSimpleName());
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
            return false;
        }
    }


    public void subscribe(Topic topic)
    {




    }

}
