/* *
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

import customException.AccountRegistrationException;
import interfaces.ServerInterface;
import interfaces.ClientInterface;
import utility.Account;
import utility.Message;
import utility.ResponseCode;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
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
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

public class Server implements ServerInterface,Callable<Integer> {

    /* topic and message management fields */
    //todo se qualcuno trova un nome migliore cambitelo quello che ci ho messo fa schifo
    private ConcurrentSkipListMap<String,ConcurrentLinkedQueue<Integer>> topicClientList;                 // topic -> lista idAccount    -   PUNTI 1 e 2
    //invece di rompere sempre le balle all'account manager dandogli id dello stub si potrebbe salvare direttamente lo stub
    private ConcurrentSkipListMap<String,ConcurrentLinkedQueue<ClientInterface>> topicClientList_2;        // topic -> lista stubClient
    //NOTA: nella mia idea le varie liste associate ai topic contengono solo i riferimenti ai client che sono attualmente online

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
    private ConcurrentHashMap<String, Integer> userNameList;            //coppia (userName_account, idAccount) degli accoun che sono salvati

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
    private String serverName;                  //TODO: da creare nel costruttore, il nome con cui si fa la bind dello serverStub sul registro
    private ServerInterface skeleton;


    /* ****************************************************************************************************************************/
    /**Costruttore
     *carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     *  se il file di config non viene trovato
     */


    public Server() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, AlreadyBoundException, RemoteException, UnknownHostException {

        //TODO             creare un nome per il server utilizzato per il registro
        serverName   = "Server_" + (int)(Math.random()*1000000);
        topicList    = new ConcurrentLinkedQueue<>();
        userNameList = new ConcurrentHashMap<>();

        System.out.println(System.getProperty("user.dir"));

        //Caricamento delle impostazioni del server memorizate su file
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

    /* ****************************************************************************************************************************/
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
            //System.setProperty("java.rmi.server.codebase","file:${workspace_loc}/Server/");
            //System.setProperty ("java.rmi.server.codebase", "http://130.251.36.239/hello.jar");
            infoStamp("Policy and codebase setted.");

            //Creating and Installing a Security Manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
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
            r.rebind("ServerInterface", stub);
            infoStamp("Server stub loaded on registry associate with the  the name \'"+serverName+"\' .");

        }catch (RemoteException e){
            errorStamp(e);
            System.exit(-1);
        }

        this.registry = r;
        this.skeleton = stub;

    }







    /* ************************************************************************************************************
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
    public ResponseCode register(String userName, String plainPassword, ClientInterface stub, String publicKey,String email)  {
        try {
            String cookie;
            if (userNameList.putIfAbsent(userName, 0) == null)  //se non c'è già un account con lo stesso nome
            {
                int posNewAccount =  registerAccount(userName, plainPassword, stub, publicKey, 0,email);
                cookie = getCookie(posNewAccount);
                userNameList.replace(userName, posNewAccount);
                pedanticInfo("Registered new client \'"+userName+"\'.");
                return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, cookie);  //OK: Nuovo client registrato
            }
            pedanticInfo("Client registration refused, username \'"+userName+"\' already used.");
            return ResponseCodeList.ClientError;
        }catch (Exception e){
            userNameList.remove(userName);
            errorStamp(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode anonymousRegister(ClientInterface stub, String publicKey)  {
        return register("AnonymousAccount","",stub,publicKey,null);
    }



    @Override
    public ResponseCode disconnect(String cookie) {
        try {
            int accountId = getAccountId(cookie);
            this.accountList.setStub(null, accountId);//Se poi si ricambia quando uno si connette non è un pò inutile impostarlo a null
            pedanticInfo(accountId + "disconnected.");
            return new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.SERVER,"disconnessione avvenuta con successo");
        }catch (BadPaddingException | IllegalBlockSizeException exc){
            return new ResponseCode(ResponseCode.Codici.R620, ResponseCode.TipoClasse.SERVER,"errore disconnessione");
        }
    }

    @Override
    public ResponseCode retrieveAccount(String username, String plainPassword, ClientInterface clientStub){
        try {
            Integer accountId = userNameList.get(username);     //Returns null if userNameList does not contain the username
            if(accountId != null) {
                Account account = accountList.getAccountCopy(accountId);
                if (account.getUsername().equals(username) && account.cmpPassword(plainPassword)) {//okay -->setto lo stub
                    accountList.setStub(clientStub, accountId);
                    pedanticInfo(username + " connected.");
                    return new ResponseCode(ResponseCode.Codici.R220, ResponseCode.TipoClasse.SERVER, "login andato a buon fine");
                }
            }
            pedanticInfo(username + " failed to connect.");
            return ResponseCodeList.LoginFailed;
        } catch (Exception e) {
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
            warningStamp(e,"subscibe() - topic"+topicName+"not found");
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



    //METODI USATI PER LA GESTIONE DEGLI ACCOUNT


    private int registerAccount(String userName, String plainPassword, ClientInterface stub, String publicKey,int accountId,String email) throws AccountRegistrationException {
        //sarebbe utile aggiungere un metodo per controllare se l'account esiste già
        //però solleva dei problemi sul testing(localhost non può avere più di un account)->soluzione chiave primaria email associata all'account

        try {
            Account account = new Account(userName, plainPassword, stub, publicKey, accountId,email);
            return accountList.addAccount(account);
        }catch (Exception exc){
            errorStamp(exc,"Unable to register new account");
            throw new AccountRegistrationException("Unable to register new account");
        }
    }


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
