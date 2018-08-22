package client;

import interfaces.ClientInterface;
import interfaces.ServerInterface;
import utility.LogFormatManager;
import utility.Message;
import utility.ResponseCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static utility.ResponseCode.Codici.R200;
import static utility.ResponseCode.Codici.R670;


public class AnonymousClient implements ClientInterface {



    static final protected int DEFAULT_REGISTRY_PORT = 1099;


    /**************************************************************************/
    /* client fields */
    protected String className = "ANONYMOUS_CLIENT";
    protected ClientInterface skeleton;               //my stub
    protected String cookie;
    protected boolean pedantic  = true;

    protected TreeSet<String> topicsSubscribed;       //topic a cui si è iscritti, todo non mi sembra che ci sia concorrenza ma se qualcuno ne trova bisoga sostituire TreeSet<> con ConcurrentSkipListSet

    /**************************************************************************/
    /* server fields */
    protected String serverName;                      //the name for the remote reference to look up
    protected ServerInterface server_stub;            //broker's stub, se è null allora non si è connessi ad alcun server
    protected String[] topicOnServer;                 //topic che gestisce il server

    /* remote registry fields */
    protected String registryHost;                    //host for the remote registry
    protected int registryPort;                       //port on which the registry accepts requests

    final protected Executor messageManager = Executors.newSingleThreadExecutor();
    final private LogFormatManager print = new LogFormatManager("ANONYMOUS_CLIENT", true);







    // ************************************************************************************************************
    //CONSTRUCTOR

    /**
     * Anonymous user's constructor
     */
    public AnonymousClient()throws RemoteException
    {
        this.skeleton     = (ClientInterface) UnicastRemoteObject.exportObject(this,0);
        topicsSubscribed  = new TreeSet<>();
    }














    /*****************************************************************************************************************
     * API ***********************************************************************************************************
     ****************************************************************************************************************/

    private void setServerInfo(String regHost, String serverName){
        if(regHost==null || regHost.isEmpty() || serverName==null || serverName.isEmpty()){
            throw new IllegalArgumentException("Invalid argument format of regHost or serverName");
        }
        this.registryHost = regHost;
        this.serverName   = serverName;
        this.registryPort = DEFAULT_REGISTRY_PORT;
    }


    /**
     * Imposta i dati del server su cui fare la connect ed esegue quest'ultima.
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
        if(server_stub!=null){      //Connessione al server avvenuta con successo
            infoStamp("Successful connection to the server.");
        }else {                     //Connessione fallita perchè non si è stato trovato il server o perchè durante la connessione c'è stato un errore
            infoStamp("Unable to reach the server.");
        }
    }

    /**
     * Si connette al server specificato dalla stringa broker e dalla porta regPort facendo il lookup
     * sul registry dell'host, utilizato per vedere se il server esiste e se è attivo
     * @return lo STUB del server se andata a buon fine, altrimenti NULL
     */
    /*
    @Deprecated
    public ServerInterface connect(){
        try {
            Registry r = LocateRegistry.getRegistry(this.registryHost, this.registryPort);
            ServerInterface server_stub = (ServerInterface) r.lookup(this.serverName);
            ResponseCode rc = server_stub.connect();
            //if(rc.IsOK())
                //this.brokerPublicKey = rc.getMessaggioInfo();
            return server_stub;
        }catch (Exception e){
            return null;
        }
    }
*/

    /**
     *Il client si registra sul server su cui si era connesso con il metodo connect() e viene settato il cookie
     * @return TRUE se registrazione andata a buon fine, FALSE altrimenti
    **/
    public boolean register(){
        if(connected()) {
            try {
                ResponseCode responseCode = server_stub.anonymousRegister(this.skeleton);
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
                server_stub.disconnect(cookie);
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
    public boolean subscribe(String topic) {
        if(connected()){
            try {
                if(topicsSubscribed.contains(topic)){
                    infoStamp("Already subscribe to the \'"+topic+"\' topic.");
                    return true;
                }
                ResponseCode response = server_stub.subscribe(this.cookie, topic);
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
                ResponseCode response = server_stub.unsubscribe(this.cookie, topic);
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
    public String[] getTopics(){
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

    /**
     * @return TRUE se si è connessi ad un server, FALSE altrimenti
     */
    public boolean connected(){
        return (server_stub != null);
    }





    /****************************************************************************************************************
     * REMOTE METHOD ************************************************************************************************
     ****************************************************************************************************************/


    @Override
    //TODO al server non importa del messaggio di risposta quindi si potrebbe mettere che ritorni void
    public ResponseCode notify(Message m) {
        ResponseCode rc;
        if(m==null) {
            rc=new ResponseCode(ResponseCode.Codici.R500, ResponseCode.TipoClasse.CLIENT, "(-) WARNING Il client ha ricevuto un messaggio vuoto");
        }else {
            rc = new ResponseCode(R200, ResponseCode.TipoClasse.CLIENT,
                    "(+) OK il client ha ricevuto il messaggio");

            messageManager.execute(() -> {
                //TODO gestione della visualizazione del messaggio
                pedanticInfo("Received new message\n" + m.toString());
            });
        }
        return rc;
    }

    @Override
    public void isAlive() {
    }

    @Override
    public ResponseCode getCode(int nAttempts){
       System.out.println("Remaining attempts:"+Integer.toString(nAttempts));
       System.out.println("Enter code:");
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            return  new ResponseCode(R200,ResponseCode.TipoClasse.CLIENT,s);
        } catch (IOException e) {
            errorStamp(e, "Unable to read user input");
        }
        return new ResponseCode(R670,ResponseCode.TipoClasse.CLIENT,
                "(-) Internal client error");
    }

    /**
     * Permette all'utente di creare una nuova password per l'account associato ad email
     * @param email l'email associata all'account
     * @param newPassword
     * @param repeatPassword
     * @return
     */

    public boolean recoverPassword(String email,String newPassword,String repeatPassword){
        try {
            ResponseCode resp = server_stub.recoverPassword(email, newPassword, repeatPassword, this.skeleton);
            if(resp.IsOK()){
                infoStamp("password successfully changed.");
                return true;
            }else{
                if(resp.getCodice()==ResponseCode.Codici.R510){
                    infoStamp("invalid arguments.");
                }else{
                    infoStamp("Unknown error.");
                }
                return false;
            }
        }catch(Exception exc){
            errorStamp("Not connected.");
        }

        return false;
    }

    @Override
    public PublicKey publicKeyExchange(byte[] serverPubKey_encrypted){
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] testSecretKey(byte[] messageEncrypted){
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[][] getAccountInfo(){
        throw new UnsupportedOperationException();
    }

    @Override
    public void newTopicNotification(String topicName){

    }


    /*****************************************************************************************************************
     * PRIVATE METHOD ************************************************************************************************
     ****************************************************************************************************************/

    protected ServerInterface connect(String regHost, String server, Integer regPort)
    {
        try {
            Registry r = LocateRegistry.getRegistry(regHost, regPort);
            ServerInterface server_stub = (ServerInterface) r.lookup(server);
            server_stub.connect();
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



    /*GETTER and SETTER*/

    public ServerInterface getServer_stub() {
        return server_stub;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public  String getClassName() {
        return this.className;
    }




    //METODI UTILIZZATI PER LA GESTIONE DELL'OUTPUT DEL CLIENT

    void errorStamp(Exception e){
       this.errorStamp(e,"");
    }

    void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("["+className+"-ERROR]: " + msg);
        System.err.println("\tException type: "      + e.getClass().getSimpleName());
        System.err.println("\tException message: "   + e.getMessage());
        e.printStackTrace();    //todo da eliminare appena la fase di debigging è finita(solo questa linea non tutto il metodo)
    }

    void errorStamp(ResponseCode r, String msg){
        System.out.flush();
        System.err.println("["+className+"-ERROR]: "  + msg);
        System.err.println("\tError code: "           + r.getCodice());
        System.err.println("\tError message: "        + r.getMessaggioInfo());
    }

    void errorStamp(String msg){
        System.out.flush();
        System.err.println("["+className+"-ERROR]: "      + msg);
    }

    protected void warningStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("["+className+"-WARNING]: " + msg);
        System.err.println("\tException type: "        + e.getClass().getSimpleName());
        System.err.println("\tException message: "     + e.getMessage());
    }

    void infoStamp(String msg){
        System.out.println("["+className+"-INFO]: " + msg);
    }

    void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }

}
