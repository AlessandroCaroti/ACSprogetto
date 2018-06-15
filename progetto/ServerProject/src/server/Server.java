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

import customException.AccountRegistrationException;
import interfaces.ServerInterface;
import interfaces.ClientInterface;
import utility.Account;
import utility.ResponseCode;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements ServerInterface,Callable<Integer> {
    //STRUTTURE DATI
    /*
    *   lista dei topic
    *   lista dei client anonimi
    *
     */
    private AccountCollectionInterface accountList;                     //monitor della lista contente tutti gli account salvati
    private ConcurrentHashMap<String, Integer> userNameList;            //coppia nome (nome_account, indice_lista) degli accoun che sono salvati
    private Properties serverSettings=new Properties();                 //setting del server
    private ConcurrentHashMap<byte[],Integer> topicCookieAssociation;   //aka  hashMap contenente le associazioni topic->accountId
    private AES aesCipher;
    private String serverPublicKey;
    private String serverPrivateKey;
    private boolean pedantic = false;           //verrà utile per il debugging      todo magari anche questo si può importare dal file di config

    /*rmi fields*/
    private Registry registry;
    private int regPort = 1099;                 //Default registry port TODO magari si può importare dal file di config
    private String host;
    private String serverName;                  //TODO: da creare nel costruttore, il nome con cui si fa la bind dello serverStub sul registro
    private ServerInterface skeleton;


    /*****************************************************************************************************************************/
    /**Costruttore
     *carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     *  se il file di config non viene trovato
     */


    public Server() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, AlreadyBoundException, RemoteException, UnknownHostException {

        //TODO             creare un nome per il server utilizzato per il registro
        serverName   = "Server_" + (int)(Math.random()*1000000);
        userNameList = new ConcurrentHashMap<>();

        //Caricamento delle impostazioni del server memorizate su file
        loadSetting("config.serverSettings");
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

    /*****************************************************************************************************************************/
    /* ********************************************************************************************************** **/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */
    /*
    Link Remote Java RMI Registry:
        http://collaboration.cmc.ec.gc.ca/science/rpn/biblio/ddj/Website/articles/DDJ/2008/0812/081101oh01/081101oh01.html
     */

    // Startup of RMI serverobject, including registration of the instantiated server object
    // with remote RMI registry
    public void start(){
        ServerInterface stub = null;
        Registry r = null;

        try {
            //Importing the security policy and ...
            System.setProperty("java.security.policy","file:./sec.policy");
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
                infoStamp("Registry find.");
            } catch (RemoteException e) {
                r = LocateRegistry.getRegistry(regPort);
                warningStamp(e, "Registry created.");
            }

            //Making the Remote Object Available to Clients
            stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0); //The 2nd argument specifies which TCP port to use to listen for incoming remote invocation requests . It is common to use the value 0, which specifies the use of an anonymous port. The actual port will then be chosen at runtime by RMI or the underlying operating system.
            infoStamp("Created server remote object.");

            //Load the server stub on the Registry
            r.rebind(serverName, stub);
            infoStamp("Server stub loaded on registry.");

        }catch (RemoteException e){
            errorStamp(e);
            System.exit(-1);
        }

        this.registry = r;
        this.skeleton = stub;

    }







    /*************************************************************************************************************
     ****    METODI REMOTI          ******************************************************************************
     *************************************************************************************************************/

    @Override
    //Usato per stabilire la connesione tra server e client
    public ResponseCode connect() {
        try {
            return  new ResponseCode( ResponseCode.Codici.R210, ResponseCode.TipoClasse.SERVER,this.serverPublicKey);
        } catch (Exception e){
            errorStamp(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode register(String userName, String plainPassword, ClientInterface stub, String publicKey)  {
        try {
            String cookie;
            if (userNameList.putIfAbsent(userName, 0) == null)  //se non c'è già un account con lo stesso nome
            {
                int posNewAccount =  registerAccount(userName, plainPassword, stub, publicKey, 0);
                cookie = getCookie(posNewAccount);
                userNameList.replace(userName, posNewAccount);
                pedanticInfo("Registered new client "+userName+".");
                return new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER, cookie);  //OK: Nuovo client registrato
            }
            pedanticInfo("Client registration refused, username \'"+userName+"\' already used.");
            return ResponseCodeList.ClientError;
        }catch (Exception e){
            errorStamp(e);
        }
        return ResponseCodeList.InternalError;
    }

    @Override
    public ResponseCode anonymousRegister(ClientInterface stub, String publicKey)  {
        return register("AnonymousAccount","",stub,publicKey);
    }



    @Override
    public ResponseCode disconnect(String cookie) {
        try {
            int accountId = getAccountId(cookie);
            this.accountList.setStub(null, accountId);
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

    }

    @Override
    public void unsubscribe(String cookie,String topicName)  {

    }

    @Override
    public void publish(String cookie) {

    }

    @Override
    public void ping()  {
    }

    @Override
    public List<String> getTopicList()  {

        return null;
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


    private int registerAccount(String userName, String plainPassword, ClientInterface stub, String publicKey,int accountId) throws AccountRegistrationException {
        //sarebbe utile aggiungere un metodo per controllare se l'account esiste già
        //però solleva dei problemi sul testing(localhost non può avere più di un account)->soluzione chiave primaria email associata all'account

        try {
            Account account = new Account(userName, plainPassword, stub, publicKey, accountId);
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



























    //METODI UTILIZZATI PER LA GESTIONE DELL'OUTPUT DEL SERVER

    private void errorStamp(Exception e){
        System.err.println("[SERVER-ERROR]");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void errorStamp(Exception e, String msg){
        System.err.println("[SERVER-ERROR]: "      + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void warningStamp(Exception e, String msg){
        System.out.println("[SERVER-WARNING]: "    + msg);
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
