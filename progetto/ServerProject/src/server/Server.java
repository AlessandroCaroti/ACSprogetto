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
import email.EmailController;
import email.EmailHandlerTLS;
import interfaces.ServerInterface;
import interfaces.ClientInterface;
import utility.Account;
import utility.AddressIp;
import utility.Message;
import utility.ResponseCode;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements ServerInterface,Callable<Integer> {

    /* topic and message management fields */
    //todo se qualcuno trova un nome migliore cambitelo quello che ci ho messo fa schifo
    private ConcurrentSkipListMap<String,ConcurrentLinkedQueue<Integer>> topicClientList;                 // topic -> lista idAccount    -   PUNTI 1 e 2
    private ConcurrentLinkedQueue<String> topicList;        //utilizzata per tenere traccia di tutti i topic e da utilizzare in getTopicList()

    /*
        1)lista dei topic
        2)associazioni topic -> lista client che si sono registrati al topic
                possibili implementazioni ConcurrentSkipListMap
        3)lista client
                possibili implementazioni concurrentLikedQueue
    */

    /* clients management fields */
    private AccountCollectionInterface accountList;                     //monitor della lista contente tutti gli account salvati
    private RandomString randomStringSession;
    private AtomicInteger anonymousCounter=new AtomicInteger(0);

    /* server settings fields */
    private Properties serverSettings=new Properties();                 //setting del server
    private boolean pedantic = true;                                    //utile per il debugging per stampare ogni avvenimento      todo magari anche questo si può importare dal file di config

    /* security fields */
    private AES aesCipher;
    private String serverPublicKey;
    private String serverPrivateKey;

    /* rmi fields */
    private Registry registry;
    private int regPort = 1099;                 //Default registry port TODO magari si può importare dal file di config
    private String host;
    private String serverName;
    private ServerInterface skeleton;

    /*email handler*/
    private EmailController emailController;



    /*****************************************************************************************************************************/
    /**Costruttore
     * Carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     * se il file di config non viene trovato
     */


    public Server() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, AlreadyBoundException, RemoteException, UnknownHostException {

        //TODO             creare un nome per il server utilizzato per il registro
        try{
            serverName   = "Server_" + this.getMyIp();
        }catch(IOException exc){//se non riesce a reperire  l'ip
            serverName   = "Server_" + (int)(Math.random()*1000000);
        }


        topicList    = new ConcurrentLinkedQueue<>();

        System.out.println(System.getProperty("user.dir"));

        //Caricamento delle impostazioni del server memorizate su file
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        loadSetting("./src/server/config.serverSettings");
        infoStamp("Server settings imported.");

        //Creazione del gestore degli account
        accountList = createAccountManager();
        infoStamp("Account monitor created.");

        //Creazione PKI del server
        setupPKI();
        infoStamp("Public key infrastructure created.");

        setupAes();
        infoStamp("Aes created.");

        //Creazione dell'email handler e avvio di quest'ultimo
        emailController=new EmailHandlerTLS("acsgroup.unige@gmail.com","password",100,587,"smtp.gmail.com");
        //emailController=new EmailHandler(serverSettings,accountList.getMAXACCOUNTNUMBER());
        emailController.startEmailHandlerManager();
        infoStamp("Email Handler created and started.");

        randomStringSession=new RandomString();
        infoStamp("Random String session created");

        //Ho spostato la roba del regestry nel metodo start

        System.out.println("SERVER PRONTO (lookup)registryName:\"ServerInterface\" on port:");
    }

    /**
     * Avvia il server
     * @return 0 quando l'utente vuole spegnere l'host,1 in caso di errore dell'interfaccia grafica
     */
    public Integer call(){

        System.out.println("Enter something here : ");

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
        pedanticInfo("Starting server ...");
        ServerInterface stub = null;
        Registry r = null;

        try {
            //Importing the security policy and ...
            System.setProperty("java.security.policy","file:./src/server/sec.policy");

            infoStamp("Policy and codebase setted.");

            //Creating and Installing a Security Manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            testPolicy(System.getSecurityManager());
            infoStamp("Security Manager installed.");

            //Creating or import the local regestry
            try {
                r = LocateRegistry.createRegistry(regPort);
                infoStamp("New registry created on port "+regPort+".");
            } catch (RemoteException e) {
                r = LocateRegistry.getRegistry(regPort);
                infoStamp("Registry find on port \"+regPort+\".");
            }

            //Making the Remote Object Available to Clients
            stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0); //The 2nd argument specifies which TCP port to use to listen for incoming remote invocation requests . It is common to use the value 0, which specifies the use of an anonymous port. The actual port will then be chosen at runtime by RMI or the underlying operating system.
            infoStamp("Created server remote object.");

            //Load the server stub on the Registry
            //r.rebind(serverName, stub);
            r.rebind(serverName, stub);
            infoStamp("Server stub loaded on registry associate with the  the name \'"+serverName+"\' .");

        }catch (RemoteException e){
            errorStamp(e);
            System.exit(-1);
        }

        this.registry = r;
        this.skeleton = stub;

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
            pedanticInfo("A new client has connected.");
            return  new ResponseCode( ResponseCode.Codici.R210, ResponseCode.TipoClasse.SERVER,this.serverPublicKey);
        } catch (Exception e){
            errorStamp(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode register(String userName,String plainPassword,ClientInterface stub,String publicKey,String email)  {
        int accountId;
        try {
            Account account=new Account(userName,plainPassword,stub,publicKey,0,email);
            if((accountId=accountList.putIfAbsentEmailUsername(account))>=0){

                if(this.emailValidation(email,stub)){
                    pedanticInfo("Registered new client \'"+userName+"\'  \'"+email+"\'");
                    return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, getCookie(accountId));
                }else{
                    pedanticInfo("Client registration refused ,\'"+email+"\' has not been validated.");
                    accountList.removeAccountCheckEmail(accountId,email);/* check sulla chiave primaria(email) per  problemi di concorrenza con un metodo tipo deleteAccount()*/
                    return  ResponseCodeList.WrongCodeValidation;
                }
            }else{//email or username already present
                if(accountId==-1){
                    pedanticInfo("Client registration refused, email \'"+email+"\' already used.");
                    sendEmailAccountInfo(email,accountList.getAccountCopyEmail(email).getUsername());
                    this.antiAccountEnum(stub);
                    return  ResponseCodeList.WrongCodeValidation;
                }
                if(accountId==-2){
                    pedanticInfo("Client registration refused, username \'"+userName+"\' already used.");
                    return ResponseCodeList.InvalidUsername;
                }
            }

        }catch (Exception e){
            errorStamp(e);
        }
        return ResponseCodeList.InternalError;
    }


    @Override
    public ResponseCode anonymousRegister(ClientInterface stub, String publicKey)  {
        int accountId;
        String username;
        String plainPassword;
        String email = "anonymous";//Nota bene:un'account anonimo ha sempre la seguente email-quindi il check per sapere se è anonimo o no si fa sulla mail
        Account account;
        try {
            do {
                username = "anonymous" + Integer.toString(anonymousCounter.incrementAndGet());
                plainPassword = randomStringSession.nextString();
                account = new Account(username, plainPassword, stub, publicKey, 0, email);
                if ((accountId = accountList.putIfAbsentUsername(account)) >= 0) {
                    pedanticInfo("Registered new client \'"+username+"\'  \'"+email+"\'");
                    return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, getCookie(accountId));

                } else {// username already present
                    if (accountId == -2) {
                        pedanticInfo("Client registration refused, username \'" + username + "\' already used. Trying to generate another one.");
                    }
                }
            }while(accountId==-2);//questo while non dovrebbe essere necessario in quanto anonymouscounter è atomic
        }catch(Exception e) {
            errorStamp(e);
        }
        return ResponseCodeList.InternalError;
    }



    @Override
    public ResponseCode disconnect(String cookie) {
        try {
            int accountId = getAccountId(cookie);
            this.accountList.setStub(null, accountId);
            pedanticInfo("user:"+accountId + "  disconnected.");
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
                    pedanticInfo(username + " connected.");
                    return new ResponseCode(ResponseCode.Codici.R220, ResponseCode.TipoClasse.SERVER, "login andato a buon fine");
                } else {
                    pedanticInfo(username + " invalid retrieve account.");
                    return ResponseCodeList.LoginFailed;
                }
            }
        }catch(Exception e){
            errorStamp(e);
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
                    pedanticInfo("anonymous"+account.getUsername() + " connected.");
                    return new ResponseCode(ResponseCode.Codici.R220, ResponseCode.TipoClasse.SERVER, "login andato a buon fine");
                }
            }else{
                pedanticInfo("Invalid cookie.");
                return ResponseCodeList.LoginFailed;
            }

        }catch(Exception e){
            errorStamp(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public void subscribe(String cookie, String topicName)  {
        try {
            Integer accountId = getAccountId(cookie);
            //todo controllare che non sia già inscritto al topic
            topicClientList.get(topicName).add(accountId);
        }catch (BadPaddingException| IllegalBlockSizeException e){
            warningStamp(e,"subscribe() - error cookies not recognize");
        }catch (NullPointerException e){
            warningStamp(e,"subscribe() - topic"+topicName+"not found");
        }catch (Exception e){
            errorStamp(e);
        }
    }

    @Override
    public void unsubscribe(String cookie,String topicName)  {
        try {
            Integer accountId = getAccountId(cookie);
            //todo fose bisogna controllare che sia già inscritto al topic
            topicClientList.get(topicName).remove(accountId);
        }catch (BadPaddingException| IllegalBlockSizeException e){
            warningStamp(e,"subscribe() - error cookies not recognize");
        }catch (NullPointerException e){
            warningStamp(e,"subscibe() - topic \'"+topicName+"\' not found");
        }catch (Exception e){
            errorStamp(e);
        }

    }

    @Override
    //Il client che invia il messaggio riceverà una copia del suo stesso messaggio, questo lo gestiremo nel client e si può usare anche come conferma dell'invio tipo la spunta blu di whatsappp
    public void publish(String cookie, Message msg) {
        try {
            Integer accountId = getAccountId(cookie);
            String topicName  = msg.getTopic();
            ConcurrentLinkedQueue<Integer> subscibers = topicClientList.putIfAbsent(topicName, new ConcurrentLinkedQueue<Integer>());
            if(subscibers == null){  //creazione di un nuovo topc
                topicList.add(topicName);
                (subscibers = topicClientList.get(topicName)).add(accountId);
            }
            notifyAll(subscibers.iterator(), msg);      //todo magari si potrebbe eseguire su un altro thread in modo da non bloccare questa funzione
        }catch (BadPaddingException| IllegalBlockSizeException e){
            warningStamp(e,"subscribe() - error cookies not recognize");
        }catch (Exception e){
            errorStamp(e);
        }
    }

    @Override
    public void ping()  {
    }

    @Override
    public String[] getTopicList()  {
        return topicList.toArray(new String[0]);    //guarda esempio in https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html#toArray(T[])
    }

    //TODO
    @Override
    public ResponseCode retrieveCookie(String username,String plainPassword){
            try {
                Account account = this.accountList.getAccountCopyUsername(username);
                if (account.cmpPassword(plainPassword)) {
                    pedanticInfo("Sending " + username + " cookie.");
                    return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, getCookie(account.getAccountId()));
                } else {
                    pedanticInfo("Invalid password.");
                    return ResponseCodeList.LoginFailed;
                }
                //nosuch
            }catch(Exception exc){
                if(exc instanceof IllegalArgumentException){
                    pedanticInfo("Invalid username (null).");
                    return ResponseCodeList.LoginFailed;
                }
                else{
                    errorStamp(exc);
                }
            }
        return ResponseCodeList.InternalError;
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
            errorStamp(e,"The file \'"+settingFileName+"\' could not be found or error occurred when reading it!");
            System.exit(-1);
        }finally {
            //Chiusura del file
            try {
                if(in != null)
                    in.close();
            } catch (IOException e) {
                warningStamp(e, "File closure failed!");
            }
        }
    }

    //Creazione della chiava pubblica, chiave privata con cui verranno criptati i messaggi scambiaticon i client
    private void setupPKI(){
        /*TODO creare le chiavi pubbliche eccetera e settarle nei fields*/
        serverPrivateKey="privatekeyservertobeimplmented";
        serverPublicKey="publickeyservertobeimplmented";
    }

    private void setupAes() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException {

        try {
            aesCipher = new AES("RandomInitVectol");        //TODO usiamo un intvector un pò migliore
        }catch (Exception exc){
            errorStamp(exc, "Unable to create aes encryption class");
            throw exc;
        }
    }

    private AccountCollectionInterface createAccountManager(){
        AccountCollectionInterface accManager;
        try {
            accManager = new AccountListMonitor(Integer.parseInt(serverSettings.getProperty("maxaccountnumber")));
        }catch (IllegalArgumentException e){
            warningStamp(e, "Creating AccountManager using default size");
            accManager = new AccountListMonitor();        //Utilizzo del costruttore di default
        }
        return accManager;
    }

    private void testPolicy(SecurityManager sm){
        try {
            sm.checkListen(0);
            //sm.checkPackageAccess("sun.rmi.*");
        }catch (Exception e){
            errorStamp(e, "Policies not imported properly");
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
                    warningStamp(e, "client not reachable.");
                    //todo il client corrente va eliminato perchè non più raggiungibile
                }catch (NullPointerException e){
                    warningStamp(e, "Client stub not saved.");
                    //todo il client corrente va eliminato perchè si è disconneddo  -   si potrebbe pensare di farlo durante la chiamata della disconnect(...)
                }
            });
    }

    private  String getMyIp() throws IOException {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            return in.readLine();
        }catch (IOException exc){
            this.errorStamp(exc,"unable to get server external ip.");
            throw exc;
        }
    }


    private boolean emailValidation(String email,ClientInterface stub) throws MessagingException, RemoteException {

        String temp;
        StringTokenizer tokenizer=new StringTokenizer(email);
        temp=tokenizer.nextToken();
        if(temp.equalsIgnoreCase("test"))return true;//TODO REMOVE 4 LINES up

        final int MAXATTEMPTS = 3;
        ResponseCode resp;
        Integer codice = (int) (Math.random() * 1000000);
        emailController.sendMessage(emailController.createEmailMessage(email, "EMAIL VALIDATION",
                "Codice verifica:" + Integer.toString(codice)
        ));
        infoStamp("message to:"+email+"; added to queue code:"+Integer.toString(codice));
        for (int i = MAXATTEMPTS; i >0 ; i--) {
            resp=stub.getCode(i);
            if (resp.IsOK()) {
                infoStamp("the user has entered the code:"+resp.getMessaggioInfo()+";");
                if(codice.equals(Integer.parseInt(resp.getMessaggioInfo()))) {
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
            infoStamp("(antiAccountEnum)the user has entered the code:"+resp.getMessaggioInfo()+";");
        }
    }

    private void sendEmailAccountInfo(String email,String username) throws MessagingException {

        String temp;
        StringTokenizer tokenizer=new StringTokenizer(email);
        temp=tokenizer.nextToken();
        if(temp.equalsIgnoreCase("test"))return;//TODO REMOVE 4 LINES up

        javax.mail.Message message=emailController.createEmailMessage(email,"REGISTRATION ATTEMPT",
                "Someone tried to register a new account by associating it with this email.\n" +
                        "If you have not made the request, ignore and delete the message.\n" +
                        "We remind you that the following email is associated with the username \'"+username+"\'\n"+
                        "The ACSgroup account team."
                );
        emailController.sendMessage(message);
    }




    //METODI UTILIZZATI PER LA GESTIONE DELL'OUTPUT DEL SERVER

    private void errorStamp(Exception e){
        System.out.flush();
        System.err.println("[SERVER-ERROR]");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[SERVER-ERROR]: "      + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void warningStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[SERVER-WARNING]: "    + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    private void infoStamp(String msg){
        System.out.println("[SERVER-INFO]: " + msg);
    }

    private void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }




}
