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

package server;

import account.AccountCollectionInterface;
import account.AccountListMonitor;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import email.EmailController;
import email.EmailHandlerTLS;
import interfaces.ClientInterface;
import interfaces.ServerInterface;
import server_gui.ServerStatistic;
import utility.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements ServerInterface,Callable<Integer> {

    /* topic and message management fields */
    private ConcurrentSkipListMap<String,ConcurrentLinkedQueue<Integer>> topicClientList;                 // topic -> lista idAccount
    private ConcurrentLinkedQueue<String> topicList;        //utilizzata per tenere traccia di tutti i topic e da utilizzare in getTopicList()
    private ConcurrentLinkedQueue<Integer> notificationList;

    /* clients management fields */
    private AccountCollectionInterface accountList;                     //monitor della lista contente tutti gli account salvati
    private RandomString randomStringSession;
    private AtomicInteger anonymousCounter = new AtomicInteger(0);

    /* server settings fields */
    private Properties serverSettings = new Properties();                 //setting del server

    /* security fields */
    private AES aesCipher;
    final private String curveName = "prime192v1";
    private KeyPair RSA_kayPair;
    private KeyPair ECDH_kayPair;
    final private PrivateKey RSA_privateKey;
    final private PrivateKey ECDH_privateKey;
    final private String     RSA_pubKey;
    final private byte[]     ECDH_pubKey_encrypted;
    final private byte[]     messageTest = "testStringForSecretKey".getBytes(StandardCharsets.UTF_8);

    /* rmi fields */
    private Registry registry;
    private int regPort = 1099;                 //Default registry port TODO magari si può importare dal file di config
    private String host;
    final private String serverName;
    private ServerInterface skeleton;

    /*email handler*/
    private EmailController emailController;

    /* GUI fields */
    private boolean graphicInterfaceReady;
    final private ServerStatistic serverStat;

    final private LogFormatManager print;



    /*****************************************************************************************************************************/
    /**Costruttore
     * Carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     * se il file di config non viene trovato
     */
    @Deprecated
    public Server() throws Exception {
        this(new ServerStatistic());
    }

    public Server(ServerStatistic serverStat) throws Exception {
        this(serverStat, false);
    }

    public Server(ServerStatistic serverStat, boolean pedantic) throws Exception {
        print = new LogFormatManager("SERVER", pedantic);

        print.info("Creating server ...");

        serverName = "Server_" + UUID.randomUUID().toString();

        topicList        = new ConcurrentLinkedQueue<>();
        notificationList = new ConcurrentLinkedQueue<>();
        topicClientList  = new ConcurrentSkipListMap<>();

        //Caricamento delle impostazioni del server memorizate su file
        print.pedanticInfo("Working Directory = " + System.getProperty("user.dir"));
        
        loadSetting("./src/server/config.serverSettings");
        print.info("Server settings imported.");

        //Creazione del gestore degli account
        accountList = createAccountManager();
        print.info("Account monitor created.");

        //Creazione PKI del server
        setupPKI();
        RSA_privateKey  = RSA_kayPair.getPrivate();
        ECDH_privateKey = ECDH_kayPair.getPrivate();
        RSA_pubKey = new String(Base64.encode(RSA_kayPair.getPublic().getEncoded()).getBytes());
        ECDH_pubKey_encrypted = RSA.encrypt(RSA_privateKey, ECDH_kayPair.getPublic().getEncoded());
        print.info("Public key infrastructure created.");

        setupAes();
        print.info("AES encryption system created.");

        //Creazione dell'email handler e avvio di quest'ultimo
        emailController=new EmailHandlerTLS("acsgroup.unige@gmail.com","password",100,587,"smtp.gmail.com");
        //emailController=new EmailHandler(serverSettings,accountList.getMAXACCOUNTNUMBER());
        emailController.startEmailHandlerManager();
        print.info("Email Handler created and started.");

        randomStringSession=new RandomString();
        print.info("Random String session created.");


        this.serverStat = Objects.requireNonNull(serverStat);
        this.serverStat.setServerInfo(this.serverName, topicList, AddressIp.getExternalAddres(), regPort);
        print.info("***** SERVER CREATED! *****");
    }

    /**
     * Avvia il server
     * @return 0 quando l'utente vuole spegnere l'host,1 in caso di errore dell'interfaccia grafica
     */
    public Integer call(){


        try{
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Closing Server with exitcode:0");
        return 0;
    }

    /*****************************************************************************************************************************/
    /* ********************************************************************************************************** **/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */
    /*
    Link spiegazione funzionamento Remote Java RMI Registry:
        http://collaboration.cmc.ec.gc.ca/science/rpn/biblio/ddj/Website/articles/DDJ/2008/0812/081101oh01/081101oh01.html
     */




    // Startup of RMI serverobject, including registration of the instantiated server object
    // with remote RMI registry
    public void start(){
        print.pedanticInfo("Starting server ...");
        ServerInterface stub = null;
        Registry r = null;

        try {
            //Importing the security policy and ...
            System.setProperty("java.security.policy","file:./src/server/sec.policy");
            print.info("Policy and codebase setted.");

            //Creating and Installing a Security Manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            testPolicy(System.getSecurityManager());
            print.info("Security Manager installed.");

            //Creating or import the local regestry
            try {
                r = LocateRegistry.createRegistry(regPort);
                print.info("New registry created on port "+regPort+".");
            } catch (RemoteException e) {
                r = LocateRegistry.getRegistry(regPort);
                print.info("Registry find on port "+regPort+".");
            }

            //Making the Remote Object Available to Clients
            stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0); //The 2nd argument specifies which TCP port to use to listen for incoming remote invocation requests . It is common to use the value 0, which specifies the use of an anonymous port. The actual port will then be chosen at runtime by RMI or the underlying operating system.
            print.info("Created server remote object.");

            //Load the server stub on the Registry
            r.rebind(serverName, stub);
            print.info("Server stub loaded on registry associate with the  the name \'"+serverName+"\'.");

        }catch (RemoteException e){
            print.error(e);
            System.exit(-1);
        }

        this.registry = r;
        this.skeleton = stub;

        serverStat.setServerReady();

        print.info("***** SERVER READY! *****");
    }


    public void stop(){
        if(registry==null)  //Nothing to do
            return;

        print.pedanticInfo("Stopping server ...");
        try {
            registry.unbind(serverName);
            UnicastRemoteObject.unexportObject(this.registry, true);

        } catch (RemoteException | NotBoundException e) {
            print.error(e);
            System.exit(-1);
        }
        registry = null;
        print.info("***** SERVER OFFLINE! *****");
    }

    /*METOGI GETTER*/
    public int getRegPort(){
        return regPort;
    }

    public String getRegHost(){
        return AddressIp.getLocalAddres();
    }

    public String getServerName(){
        return serverName;
    }


    /*************************************************************************************************************
     ****    METODI REMOTI          ******************************************************************************
     *************************************************************************************************************/

    @Override
    //Usato per stabilire la connesione tra server e client
    public ResponseCode connect() {
        try {
            print.pedanticInfo("A new client has connected.");
            return  new ResponseCode( ResponseCode.Codici.R210, ResponseCode.TipoClasse.SERVER, RSA_pubKey);
        } catch (Exception e){
            print.error(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode register(ClientInterface stub)  {
        int accountId;
        SecretKeySpec secretAesKey;
        String userName=null, plainPassword=null, email=null, publicKey=null;
        //CREAZIONE DI UNA CHIAVE CONDIVSA SOLO TRA IL SERVER E IL CLIENT CHE HA INVOCATO QUESTO METODO REMOTO
        try {
            //compute the key
            PublicKey clientPubKey = stub.publicKeyExchange(ECDH_pubKey_encrypted);
            byte[] shearedSecretKey = ECDH.sharedSecretKey(ECDH_privateKey, clientPubKey);
            secretAesKey = new SecretKeySpec(shearedSecretKey, "AES");
            
            //test the key
            byte[] res_encrypted = stub.testSecretKey(messageTest);
            byte[] res = utility.AES.decrypt(res_encrypted, secretAesKey);
            if(!Arrays.equals(res, messageTest))
                throw new InvalidKeyException("La chiave condivisa non coincide");  //todo migliorare il messaggio di errore
            publicKey     = new String(shearedSecretKey);
        } catch (RemoteException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            //todo aggiunere migliore gestione degli errori
            print.error(e);
            return ResponseCodeList.InternalError;
        }
        try {
            //RECUPERO DELLE INFORMAZIONI DEL CLIENT
            byte[][] accountInfo = stub.getAccountInfo();
            email         = new String(utility.AES.decrypt(accountInfo[0], secretAesKey), StandardCharsets.UTF_8);
            userName      = new String(utility.AES.decrypt(accountInfo[1], secretAesKey), StandardCharsets.UTF_8);
            plainPassword = new String(utility.AES.decrypt(accountInfo[2], secretAesKey), StandardCharsets.UTF_8);
            //CREAZIONE DI UN ACCOUNT PER IL CLIENT
            Account account=new Account(userName,plainPassword,stub,publicKey,0,email);
            if((accountId=accountList.putIfAbsentEmailUsername(account))>=0){

                if(this.emailValidation(email,stub)){
                    print.info("Registered new client, UserName: \'"+userName+"\' - Email: \'"+email+"\'  Password:"+plainPassword+"\n");
                    serverStat.incrementClientNum();
                    return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, getCookie(accountId));
                }else{
                    print.info("Client registration refused ,\'"+email+"\' has not been validated.");
                    accountList.removeAccountCheckEmail(accountId,email);/* check sulla chiave primaria(email) per  problemi di concorrenza con un metodo tipo deleteAccount()*/
                    return  ResponseCodeList.WrongCodeValidation;
                }
            }else{//email or username already present
                if(accountId==-1){
                    print.info("Client registration refused, email \'"+email+"\' already used.");
                    sendEmailAccountInfo(email,accountList.getAccountCopyEmail(email).getUsername());
                    this.antiAccountEnum(stub);
                    return  ResponseCodeList.WrongCodeValidation;
                }
                if(accountId==-2){
                    print.info("Client registration refused, username \'"+userName+"\' already used.");
                    return ResponseCodeList.InvalidUsername;
                }
            }

        }catch (Exception e){
            print.error(e);
        }
        return ResponseCodeList.InternalError;
    }


    @Override
    public ResponseCode anonymousRegister(ClientInterface stub)  {
        int accountId;
        String username;
        String plainPassword;
        String email = "anonymous";//Nota bene:un'account anonimo ha sempre la seguente email-quindi il check per sapere se è anonimo o no si fa sulla mail
        Account account;
        try {
            do {
                username = "anonymous" + Integer.toString(anonymousCounter.incrementAndGet());
                plainPassword = randomStringSession.nextString();
                account = new Account(username, plainPassword, stub, null, 0, email);   //todo la chiave pubblica per l'account anonimo non serve
                if ((accountId = accountList.putIfAbsentUsername(account)) >= 0) {
                    print.pedanticInfo("Registered new client \'"+username+"\'  \'"+email+"\'");
                    serverStat.incrementClientNum();
                    return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, getCookie(accountId));
                } else {// username already present
                    if (accountId == -2) {
                        print.pedanticInfo("Client registration refused, username \'" + username + "\' already used. Trying to generate another one.");
                    }
                }
            }while(accountId==-2);//questo while non dovrebbe essere necessario in quanto anonymouscounter è atomic
        }catch(Exception e) {
            print.error(e);
        }
        return ResponseCodeList.InternalError;
    }



    @Override
    public ResponseCode disconnect(String cookie) {
        try {
            int accountId = getAccountId(cookie);
            this.accountList.setStub(null, accountId);
            //todo creare una funzione invalidateTemporantInfo() che imposta a null lo stub e la chiaveSegretaCondivisa
            print.pedanticInfo("User "+accountId + "  disconnected.");
            serverStat.decrementClientNum();
            return new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.SERVER,"disconnessione avvenuta con successo");
        }catch (BadPaddingException | IllegalBlockSizeException exc){
            return new ResponseCode(ResponseCode.Codici.R620, ResponseCode.TipoClasse.SERVER,"errore disconnessione");
        }
    }

    @Override
    public ResponseCode retrieveAccount(String username,String plainPassword,ClientInterface clientStub){
        try{
            Account account=accountList.getAccountCopyUsername(username);
            if(account!=null) {
                if (account.cmpPassword(plainPassword)) {
                    accountList.setStub(clientStub, account.getAccountId());
                    print.pedanticInfo(username + " connected.");
                    serverStat.incrementClientNum();
                    return new ResponseCode(ResponseCode.Codici.R220, ResponseCode.TipoClasse.SERVER, "login andato a buon fine");
                } else {
                    print.pedanticInfo(username + " invalid retrieve account.");
                    return ResponseCodeList.LoginFailed;
                }
            }
        }catch(Exception e){
            print.error(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode retrieveAccountByCookie(String cookie,String plainPassword,ClientInterface clientStub){
        try{
            int accountId=this.getAccountId(cookie);
            Account account=accountList.getAccountCopy(accountId);
            if(account!=null){
                if(account.cmpPassword(plainPassword)){
                    accountList.setStub(clientStub, account.getAccountId());
                    print.pedanticInfo(account.getUsername() + " connected.(cookie):"+cookie);
                    serverStat.incrementClientNum();
                    return new ResponseCode(ResponseCode.Codici.R220, ResponseCode.TipoClasse.SERVER, "login andato a buon fine");
                }
            }else{
                print.pedanticInfo("Invalid cookie.");
                return ResponseCodeList.LoginFailed;
            }

        }catch(Exception e){
            print.error(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode subscribe(String cookie, String topicName)  {
        try {
            Integer accountId=getAccountId(cookie);
            if(!topicList.contains(topicName)){//topic inesistente
                print.pedanticInfo("User "+accountId + " searched for "+topicName+".");
                return new ResponseCode(ResponseCode.Codici.R640,ResponseCode.TipoClasse.SERVER,"topic inesistente");
            }
            ConcurrentLinkedQueue<Integer>subscribers=topicClientList.get(topicName);
            if(!subscribers.contains(accountId)){
                print.pedanticInfo("User "+accountId + "  subscribed to "+topicName+".");
                subscribers.add(accountId);
            }
            return new ResponseCode(ResponseCode.Codici.R200,ResponseCode.TipoClasse.SERVER,"iscrizione avvenuta con successo");
        }
        catch (BadPaddingException| IllegalBlockSizeException e){
            print.warning(e,"subscribe() - error cookies not recognize");
        }catch (Exception e){
           print.error(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode unsubscribe(String cookie,String topicName)  {
        try {
            Integer accountId = getAccountId(cookie);
            topicClientList.get(topicName).remove(accountId);
            print.pedanticInfo("User:"+accountId + " unsubscribe from "+topicName+".");
            return new ResponseCode(ResponseCode.Codici.R200,ResponseCode.TipoClasse.SERVER,"disiscrizione avvenuta con successo");
        }catch (BadPaddingException| IllegalBlockSizeException e){
            print.warning(e,"subscribe() - error cookies not recognize");
        } catch (Exception e){
            print.error(e);
        }
        return ResponseCodeList.InternalError;
    }

    //todo bisognerebbe controllare che il field msg.autore sia uguale a quello ricavato dal cookie---> altrimenti "spoofing" sugli autori dei messaggi!
    @Override
    //Il client che invia il messaggio riceverà una copia del suo stesso messaggio, questo lo gestiremo nel client e si può usare anche come conferma dell'invio tipo la spunta blu di whatsappp
    public ResponseCode publish(String cookie, Message msg) {
        try {
            Integer accountId = getAccountId(cookie);
            String topicName  = msg.getTopic();
            ConcurrentLinkedQueue<Integer> subscribers = topicClientList.putIfAbsent(topicName, new ConcurrentLinkedQueue<>());
            if(subscribers == null){  //creazione di un nuovo topic
                print.pedanticInfo("User "+accountId + " has created a new topic named \'"+topicName+"\'.");
                topicList.add(topicName);
                (subscribers = topicClientList.get(topicName)).add(accountId);
                serverStat.incrementTopicNum();
            }
            notifyAll(subscribers.iterator(), msg);      //todo magari si potrebbe eseguire su un altro thread in modo da non bloccare questa funzione
            serverStat.incrementPostNum();

            return new ResponseCode(ResponseCode.Codici.R200,ResponseCode.TipoClasse.SERVER,"topic pubblicato");
        }catch (BadPaddingException| IllegalBlockSizeException e){
            print.warning(e,"subscribe() - error cookie not recognized");
        }catch (Exception e){
            print.error(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public void ping()  {
    }

    @Override
    public String[] getTopicList()  {
        return topicList.toArray(new String[0]);    //per spiegazioni a cosa server 'new String[0]' guarda esempio in https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html#toArray(T[])
    }

    @Override
    public ResponseCode retrieveCookie(String username,String plainPassword){
            try {
                Account account = this.accountList.getAccountCopyUsername(username);
                if (account.cmpPassword(plainPassword)) {
                    print.pedanticInfo("Sending " + username + " cookie.");
                    return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, getCookie(account.getAccountId()));
                } else {
                    print.pedanticInfo("Invalid password.");
                    return ResponseCodeList.LoginFailed;
                }
                //nosuch
            }catch(Exception exc){
                if(exc instanceof IllegalArgumentException){
                    print.pedanticInfo("Invalid username (null).");
                    return ResponseCodeList.LoginFailed;
                }
                else{
                    print.error(exc);
                }
            }
        return ResponseCodeList.InternalError;
    }

    /*************************************************************************************************************
     ****    METODI PROTECTED       ******************************************************************************
     *************************************************************************************************************/

    protected void forwardMessage(Message msg){

            String topicName  = msg.getTopic();
            ConcurrentLinkedQueue<Integer> subscribers = topicClientList.putIfAbsent(topicName, new ConcurrentLinkedQueue<Integer>());
            if(subscribers == null){  //creazione di un nuovo topic
                topicList.add(topicName);
            }
            notifyAll(subscribers.iterator(), msg);      //todo magari si potrebbe eseguire su un altro thread in modo da non bloccare questa funzione
    }


     @Override
     public ResponseCode subscribeNewTopicNotification(String cookie){

        try {
            Integer accountId = getAccountId(cookie);
            synchronized (notificationList) {
                if (!notificationList.contains(accountId)) {
                    notificationList.add(accountId);
                }
            }
            return new ResponseCode(ResponseCode.Codici.R200,ResponseCode.TipoClasse.SERVER,"iscrizione avvenuta con successo");
        }catch (Exception e){
            print.error(e);
        }
        return ResponseCodeList.InternalError;
     }

     @Override
     public ResponseCode recoverPassword(String email,String newPassword,String repeatPassword,ClientInterface stubCurrentHost){//il current host potrebbe essere diverso da quello salvtao nella classe account
        Account copy;
         if (newPassword == null || repeatPassword == null) {
            return new ResponseCode(ResponseCode.Codici.R510,ResponseCode.TipoClasse.SERVER,"newpassword or password ==null");
         }
         if(newPassword.isEmpty()){
             return new ResponseCode(ResponseCode.Codici.R510,ResponseCode.TipoClasse.SERVER,"newpassword is empty");
         }
         if (!newPassword.equals(repeatPassword)) {
             return new ResponseCode(ResponseCode.Codici.R510,ResponseCode.TipoClasse.SERVER,"newpassword != password");
         }

             try {
                 if ((copy = accountList.isMember(email, null)) == null) {//l'account non esiste
                     print.pedanticInfo("Password recover refused ,\'" + email + "\' doesn't exist.(possible attempt to enumerate accounts!)");
                     this.antiAccountEnum(stubCurrentHost);
                     return  ResponseCodeList.WrongCodeValidation;
                 }
                 else {//l'account esiste
                     if (this.emailValidation(email, stubCurrentHost)) {
                         print.pedanticInfo("Password recovered! UserName: \'" + copy.getUsername() + "\' - NewPassword: \'" + newPassword + "\'");
                         accountList.setPassword(newPassword,copy.getAccountId());//todo probabile bug sulla concorrenza se qualcuno fa una deleteaccount( ma noi non la diamo disponibile quindi scialla)
                         return new ResponseCode(ResponseCode.Codici.R220, ResponseCode.TipoClasse.SERVER,"password successfully changed.");
                     } else {
                         print.pedanticInfo("Client password recovering refused; wrong code.");
                         return ResponseCodeList.WrongCodeValidation;
                     }

                 }

             } catch (Exception e) {
                print.error(e);
             }

         return ResponseCodeList.InternalError;
     }



    /*************************************************************************************************************
     ****    METODI PKG             ******************************************************************************
     *************************************************************************************************************/


    void addTopic(String topic){
         if(topic==null) throw new NullPointerException("topic==null");
         if(topic.isEmpty()) throw new IllegalArgumentException("topic is empty");
         synchronized (topicList){
             if(!topicList.contains(topic)){
                 topicList.add(topic);
             }
         }
    }
    /*************************************************************************************************************
    ****    METODI PRIVATI          ******************************************************************************
    *************************************************************************************************************/


    //METODI UTILIZZATI PER LA CREAZIONE DEL SERVER

    private void loadSetting(String settingFileName){
        FileInputStream in = null;
        try {
            //Apertura del file
            in = new FileInputStream(settingFileName);
            //Caricamnto delle impostazioni
            serverSettings.load(in);
        } catch (IOException e) {
            print.error(e,"The file \'"+settingFileName+"\' could not be found or error occurred when reading it!");
            System.exit(-1);
        }finally {
            //Chiusura del file
            try {
                if(in != null)
                    in.close();
            } catch (IOException e) {
                print.warning(e, "File closure failed!");
            }
        }
    }

    //Creazione della chiava pubblica, chiave privata con cui verranno criptati le informazioni sensibili scambiate col client
    private void setupPKI(){
        try {
            RSA_kayPair = RSA.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            print.error(e, "Error during generation of the keys for the RSA algorithm.");
            System.exit(1);
        }
        try {
            ECDH_kayPair = ECDH.generateKeyPair(curveName);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            print.error(e, "Error during generation of the keys for the ECDH algorithm.");
            System.exit(1);
        }
    }

    private void setupAes() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException {

        try {
            aesCipher = new AES("RandomInitVectol");        //TODO usiamo un intvector un pò migliore
        }catch (Exception exc){
            print.error(exc, "Unable to create aes encryption class.");
            throw exc;
        }
    }

    private AccountCollectionInterface createAccountManager(){
        AccountCollectionInterface accManager;
        try {
            accManager = new AccountListMonitor(Integer.parseInt(serverSettings.getProperty("maxaccountnumber")));
        }catch (IllegalArgumentException e){
            print.warning(e, "Creating AccountManager using default size.");
            accManager = new AccountListMonitor();        //Utilizzo del costruttore di default
        }
        return accManager;
    }

    private void testPolicy(SecurityManager sm){
        try {
            sm.checkListen(0);
            //sm.checkPackageAccess("sun.rmi.*");
        }catch (Exception e){
            print.error(e, "Policies not imported properly!");
            System.exit(1);
        }
    }




    /*************************************************************************************************************
     ****METODI USATI PER LA GESTIONE DEGLI ACCOUNT***************************************************************
     *************************************************************************************************************/


    private String getCookie(int accountId) throws BadPaddingException, IllegalBlockSizeException {
        return aesCipher.encrypt(String.valueOf(accountId));
    }

    private int getAccountId(String cookie) throws BadPaddingException, IllegalBlockSizeException {
        return Integer.parseInt(aesCipher.decrypt(cookie));
    }



    private void notifyAll(Iterator<Integer> accounts, Message msg){

            accounts.forEachRemaining(accountId -> {
                try {
                    ClientInterface stub = accountList.getStub(accountId);
                    stub.notify(msg);
                }catch (java.rmi.RemoteException e){
                    print.warning(e, "Client unreachable.");
                    this.accountList.setStub(null, accountId);
                }catch (NullPointerException e){
                    print.warning(e, "The client has just disconnected.");
                }
            });
    }

    private  String getMyIp() throws IOException {  //todo codice duplicato
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            return in.readLine();
        }catch (IOException exc){
            this.print.error(exc,"Unable to get server external ip.");
            throw exc;
        }
    }


    private boolean emailValidation(String email,ClientInterface stub) throws MessagingException, RemoteException {

        Integer x=-1;
        String temp;
        StringTokenizer tokenizer=new StringTokenizer(email);
        temp=tokenizer.nextToken();
        if(temp.equalsIgnoreCase("test"))return true;//TODO REMOVE 4 LINES up (sono per il testing)

        final int MAXATTEMPTS = 3;
        ResponseCode resp;
        Integer codice = (int) (Math.random() * 1000000);
        emailController.sendMessage(emailController.createEmailMessage(email, "EMAIL VALIDATION",
                "Codice verifica:" + Integer.toString(codice)
        ));
        print.info("Message to: "+email+"; added to queue code: "+Integer.toString(codice)+".");
        for (int i = MAXATTEMPTS; i >0 ; i--) {
            resp=stub.getCode(i);
            if (resp.IsOK()) {
                print.pedanticInfo("the user has entered the code:"+resp.getMessaggioInfo()+";");
                if(codice.equals(Integer.parseInt(resp.getMessaggioInfo()))||x.equals(Integer.parseInt(resp.getMessaggioInfo()))) {                              //todo remove backdoor (Integer.parseInt(resp.getMessaggioInfo())==-1) and var x
                    return true;
                }
            }
        }
        return false;
    }

    /*fa finta di fare una emailValidation per non permettere di listare le email registrate al server*/
    private void antiAccountEnum(ClientInterface stub) throws RemoteException {
        final int MAXATTEMPTS = 3;
        ResponseCode resp;
        for (int i = MAXATTEMPTS; i >0 ; i--) {
            resp=stub.getCode(i);
            print.info("(antiAccountEnum)the user has entered the code:"+resp.getMessaggioInfo()+";");
        }
    }

    private void sendEmailAccountInfo(String email,String username) throws MessagingException {

        String temp;
        StringTokenizer tokenizer=new StringTokenizer(email);
        temp=tokenizer.nextToken();
        if(temp.equalsIgnoreCase("test"))return;//TODO REMOVE 4 LINES up (sono per il testing)

        javax.mail.Message message=emailController.createEmailMessage(email,"REGISTRATION ATTEMPT",
                "Someone tried to register a new account by associating it with this email.\n" +
                        "If you have not made the request, ignore and delete the message.\n" +
                        "We remind you that the following email is associated with the username \'"+username+"\'\n\n"+
                        "The ACSgroup account team."
                );
        emailController.sendMessage(message);
    }

}
