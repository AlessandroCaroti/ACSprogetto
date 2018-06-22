package client;

import interfaces.ClientInterface;
import interfaces.ServerInterface;
import utility.Message;
import utility.ResponseCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.TreeSet;

import static utility.ResponseCode.Codici.R200;
import static utility.ResponseCode.Codici.R670;


public class AnonymousClient implements ClientInterface {

    static final private String className = "ANONYMOUS_CLIENT";

    static final protected int DEFAULT_REGISTRY_PORT = 1099;


    /**************************************************************************/
    /* client fields */
    private String username;
    private ClientInterface skeleton;               //my stub
    private String cookie;
    private String myPrivateKey;
    private String myPublicKey;
    private boolean pedantic  = true;

    private TreeSet<String> topicsSubscribed;       //topic a cui si è iscritti, todo non mi sembra che ci sia concorrenza ma se qualcuno ne trova bisoga sostituire TreeSet<> con ConcurrentSkipListSet

    /**************************************************************************/
    /* server fields */
    private String serverName;                      //the name for the remote reference to look up
    private String brokerPublicKey;                 //broker's public key
    private ServerInterface server_stub;            //broker's stub, se è null allora non si è connessi ad alcun server
    private String[] topicOnServer;                 //topic che gestisce il server

    /* remote registry fields */
    private String registryHost;                    //host for the remote registry
    private int registryPort;                       //port on which the registry accepts requests







    // ************************************************************************************************************
    //CONSTRUCTOR

    /**
     * Anonymous user's constructor
     * @param username          il mio username
     * @param my_private_key    la mia chiave privata
     * @param my_public_key     la mia chiave pubblica
     */
    public AnonymousClient(String username, String my_public_key, String my_private_key)throws RemoteException
    {
        if(username==null || my_public_key==null || my_private_key==null)
            throw new NullPointerException();
        this.username     = username;
        this.myPublicKey  = my_public_key;
        this.myPrivateKey = my_private_key;
        this.skeleton     = (ClientInterface) UnicastRemoteObject.exportObject(this,0);
        topicsSubscribed  = new TreeSet<>();
    }














    // *************************************************************************************************************
    //API

    public void setServerInfo(String regHost, String serverName){
        if(regHost==null || regHost.isEmpty() || serverName==null || serverName.isEmpty()){
            throw new IllegalArgumentException("Invalid argument format of regHost or serverName");
        }
        this.registryHost = regHost;
        this.serverName   = serverName;
        this.registryPort = DEFAULT_REGISTRY_PORT;
    }


    /**
     *
     * @param regHost       indirizzo dell'host del registry
     * @param regPort       porta in cui il registry accetta richieste
     * @param serverName    il nome con cui il server ha fatto la bind sul registry del suo stub
     * @throws IllegalArgumentException se uno dei campi passati non è valido
     */
    public void setServerInfo(String regHost, int regPort, String serverName) throws IllegalArgumentException{
        setServerInfo(regHost, serverName);
        if(regPort>1024 && regPort<=65535)  //Se la porta passata è valida impostala come porta del server altrimenti prova ad usare quella di default
            this.registryPort = regPort;
        this.server_stub = connect(regHost, serverName, regPort);
        if(server_stub!=null){      //Connesione al server avvenuta con successo
            infoStamp("Successful connection to the server.");
        }else {                     //Connesione fallita perchè non si è trovato il server o perchè durante la connessione c'è stato un errore
            infoStamp("Unable to reach the server.");
        }
    }

    /**
     * Si connette al server specificato dalla stringa broker e dalla porta regPort facendo il lookup
     * sul registry dell'host, utilizato per vedere se il server esiste e se è attivo
     * @return lo STUB del server se andata a buon fine, altrimenti NULL
     */
    public ServerInterface connect(){
        try {
            Registry r = LocateRegistry.getRegistry(this.registryHost, this.registryPort);
            ServerInterface server_stub = (ServerInterface) r.lookup(this.serverName);
            ResponseCode rc = server_stub.connect();
            if(rc.IsOK())
                this.brokerPublicKey = rc.getMessaggioInfo();
            return server_stub;
        }catch (Exception e){
            return null;
        }
    }


    /**
     *Il client si registra sul server su cui si era connesso con il metodo connect() e viene settato il cookie
     * @return TRUE se registrazione andata a buon fine, FALSE altrimenti
    **/
    public boolean register(){
        if(connected()) {
            try {
                ResponseCode responseCode = server_stub.anonymousRegister(this.skeleton, this.myPublicKey);
                return registered(responseCode);
            } catch (RemoteException e) {
                errorStamp(e, "Unable to reach the server.");
            }
        }
        else
            errorStamp("Not connected to any server.");
        return false;
    }


    /**
     * Si disconnette al server a cui si è connessi. Nonostante il server ci invii un messaggio di risposta questo
     * viene ignorato perchè al client non importa.
     * @return TRUE se ci si è disconnesi con successo, FALSE altrimenti
     */
    public boolean disconnect(){
        if(connected()) {
            try {
                ResponseCode response = server_stub.disconnect(cookie);
                return true;
            } catch (RemoteException exc) {
                System.err.println(exc.getClass().getSimpleName());
            } finally {
                //Il fatto che lo stub sia null significa che non si è connessi da alcun server
                this.server_stub = null;
            }
        }
        errorStamp("Not connected to any server.");
        return false;
    }


    /**
     * Si iscrive al topic passato come argomento
     * @param topic a cui ci si vuole iscrivere
     * @return TRUE se andata a buon fine, FALSE altrimenti
     */
    public boolean subscribe(String topic)    {
        if(connected()){
            try {
                if(topicsSubscribed.contains(topic)){
                    infoStamp("Already subscribe to the \'"+topic+"\' topic.");
                    return true;
                }
                ResponseCode response=null;
                //todo aggiungere un codice di risposta alla subscribe
                /*response = */server_stub.subscribe(this.cookie, topic);
                if(response.IsOK())
                {
                    topicsSubscribed.add(topic);
                    infoStamp("Successfully subscribe to the topic \'"+topic+"\'.");
                    return true;
                }
                else {
                    errorStamp(response, "Topic subscription failed.");
                }
            }catch (RemoteException e){
                errorStamp(e, "Unable to reach the server.");
            }
        }
        else
            errorStamp("Not connected to any server.");
        return false;
    }


    /**
     * Si disiscrive al topic passato come argomento
     * @param topic a cui ci si vuole iscrivere
     * @return TRUE se andata a buon fine, FALSE altrimenti
     */
    public boolean unsubscribe(String topic)
    {
        if(connected()){
            try {
                if(!topicsSubscribed.contains(topic)){
                    infoStamp("Topic \'"+topic+"\' not included in the list of subscriptions");
                    return true;
                }
                ResponseCode response=null;
                //todo aggiungere un codice di risposta alla unsubscribe
                /*response = */server_stub.unsubscribe(this.cookie, topic);
                if(response.IsOK())
                {
                    topicsSubscribed.remove(topic);
                    infoStamp("Successfully unsubscribe to the topic \'"+topic+"\'.");
                    return true;
                }
                else {
                    errorStamp(response, "Topic unsubscription failed.");
                }
            }catch (RemoteException e){
                errorStamp(e, "Unable to reach the server.");
            }
        }
        else
            errorStamp("Not connected to any server.");
        return false;
    }


    /**
     * @return un array con tutti i topic che il server, a cui si è connessi, gestisce. Ritorna null in caso di errore
     */
    //TODO magari invece di richiedre sempre la lista dei topic si può implementare un metodo get condizionale( get-if-modified-since) tipo http
    public String[] getTocpics(){
        String[] allTopics = null;
        if(connected()){
            try {
                allTopics = server_stub.getTopicList();
                if(allTopics!=null)
                    //caching
                    topicOnServer = allTopics;
            }catch (RemoteException e){
                //invalid cache
                topicOnServer = null;
                errorStamp(e, "Unable to reach the server.");
            }
        }
        return topicOnServer;
    }


    /**
     * @return un iterator dei topic a cui si è iscritti
     */
    public Iterator<String> getTopicsSubscribed(){
        return topicsSubscribed.iterator();
    }

    /**
     * Metodo non supportato
     * @return false
     */
    public boolean retrieveAccount(){
        throw new UnsupportedOperationException();
    }

    /**
     * Metodo non supportato
     * @return false
     */
    public boolean publish( String topic, String title, String text) {
        throw new UnsupportedOperationException();
    }






    // *************************************************************************************************************
    //REMOTE METHOD

    @Override
    //TODO al server non importa del messaggio di risposta quindi si potrebbe mettere che ritorni void
    public ResponseCode notify(Message m) {
        ResponseCode rc;
        if(m==null) {
            rc=new ResponseCode(ResponseCode.Codici.R500, ResponseCode.TipoClasse.CLIENT,
                    "(-) NOT OK Il server ha ricevuto un messaggio vuoto");
            pedanticInfo("Receved new message\n"+m.toString());
            return rc;
        }
        rc=new ResponseCode(R200, ResponseCode.TipoClasse.CLIENT,
                "(+) OK il server ha ricevuto il messaggio");
        return rc;
    }

    @Override
    public void isAlive() {
    }

    @Override
    public ResponseCode getCode(int nAttempts){
       System.out.println("Remaining attempts:"+Integer.toString(nAttempts));
       System.out.println("Enter code:");
       //TODO modificare qui l'inserimento

        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            return  new ResponseCode(R200,ResponseCode.TipoClasse.CLIENT,
                    s);
        } catch (IOException e) {
            errorStamp(e, "Unable to read user input");
        }
        return new ResponseCode(R670,ResponseCode.TipoClasse.CLIENT,
                "(-) Internal client error");
    }



    // *************************************************************************************************************
    //PRIVATE METHOD

    protected ServerInterface connect(String regHost, String server, Integer regPort)
    {
        try {
            Registry r = LocateRegistry.getRegistry(regHost, regPort);
            ServerInterface server_stub = (ServerInterface) r.lookup(server);
            ResponseCode rc = server_stub.connect();
            if(rc.IsOK())
                this.brokerPublicKey = rc.getMessaggioInfo();
            return server_stub;
        }catch (Exception e){
            return null;
        }
    }

    protected boolean registered(ResponseCode response){
        if(response == null || !response.getCodice().equals(ResponseCode.Codici.R100)) {     //Registrazione fallita
            errorStamp(response, "Server registration failed");
            return false;
        }

        //Registrazione avvenuta con successo
        this.cookie = response.getMessaggioInfo();
        infoStamp("Successfully registered on server "+serverName+".");
        return true;
    }

    /**
     * @return TRUE se si è connessi ad un server, FALSE altrimenti
     */
    protected boolean connected(){
        return (server_stub != null);
    }




































    //METODI UTILIZZATI PER LA GESTIONE DELL'OUTPUT DEL CLIENT

    protected void errorStamp(Exception e){
        System.out.flush();
        System.err.println("["+className+"-ERROR]");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();    //todo da eliminare appena la fase di debigging è finita(solo questa linea non tutto il metodo)
    }

    protected void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("["+className+"-ERROR]: " + msg);
        System.err.println("\tException type: "      + e.getClass().getSimpleName());
        System.err.println("\tException message: "   + e.getMessage());
        e.printStackTrace();    //todo da eliminare appena la fase di debigging è finita(solo questa linea non tutto il metodo)
    }

    protected void errorStamp(ResponseCode r, String msg){
        System.out.flush();
        System.err.println("["+className+"-ERROR]: "  + msg);
        System.err.println("\tError code: "           + r.getCodice());
        System.err.println("\tError message: "        + r.getMessaggioInfo());
    }

    protected void errorStamp(String msg){
        System.out.flush();
        System.err.println("["+className+"-ERROR]: "      + msg);
    }

    protected void warningStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("["+className+"-WARNING]: " + msg);
        System.err.println("\tException type: "        + e.getClass().getSimpleName());
        System.err.println("\tException message: "     + e.getMessage());
    }

    protected void infoStamp(String msg){
        System.out.println("["+className+"-INFO]: " + msg);
    }

    protected void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }

}
