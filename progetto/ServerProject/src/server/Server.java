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
import utility.Topic;
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

    /*rmi fields*/
    private Registry registry;
    private int regPort = 1099;                 //Default registry port TODO magari si può importare del file di configurazione
    private String host;
    private String serverName;                  //TODO: da creare nel costruttore
    private ServerInterface skeleton;


    /*****************************************************************************************************************************/
    /**Costruttore
     *carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     *  se il file di config non viene trovato
     */


    public Server() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, AlreadyBoundException, RemoteException, UnknownHostException {

        //TODO             creare un nome per il server utilizzato per il registro
        serverName = "Server_" + (int)(Math.random()*1000000);

        //Caricamento delle impostazioni del server memorizate su file
        loadSetting("config.serverSettings");
        infoStamp("Server settings imported.");

        //Creazione del gestore degli account
        accountList = createAccountManager();
        infoStamp("Account monitor created.");

        //Creazione PKI del server
        setupPKI();
        infoStamp("Public key infrastructure created.");


        //Creazione dello stub del client, del regestry
        this.skeleton = (ServerInterface) UnicastRemoteObject.exportObject(this, 1099);//TODO CHANGE PORT HERE? - BY ALE: 1099 è la porta di default del registry
        this.registry= LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress());
        try {
            this.registry.rebind("ServerInterface", skeleton);
        }catch(NoSuchObjectException e){
            System.out.println("[SERVER-WARNING]: "+e.getClass().getSimpleName()+"-->forzo l'avvio del registry");
            this.registry = LocateRegistry.createRegistry(1099);
            this.registry = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress());
            this.registry.rebind("ServerInterface", skeleton);
        }
        System.out.println("SERVER PRONTO (lookup)registryName:\"ServerInterface\" on port:");

        try {
            aesCipher = new AES("RandomInitVectol");//TODO usiamo un intvector un pò migliore
        }catch (Exception exc){
            System.err.println("Unable to create aes encryption class:"+exc.getClass().getSimpleName());
            throw exc;
        }
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



    /*****************************************************************************************************************************/
    //METODI REMOTI

    //TODO creare una lista concorrente degli username già presenti !! e quando un utente tenta la register con un nome utente già presente ritornare un response code R610
    @Override
    public ResponseCode register(String userName, String plainPassword, ClientInterface stub, String publicKey)  {
        String cookie;
        ResponseCode responseCode;
        try{
           cookie=getCookie(registerAccount(userName, plainPassword, stub, publicKey, 0/*lo setta automaticamente dopo*/));
           return responseCode=new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER,cookie);

       } catch (Exception exc){
            return responseCode=new ResponseCode(ResponseCode.Codici.R610, ResponseCode.TipoClasse.SERVER,"Registrazione account fallita!");
        }
    }

    @Override
    public ResponseCode anonymousRegister(ClientInterface stub, String publicKey)  {
        return register("AnonymousAccount","",stub,publicKey);
    }

    @Override
    public ResponseCode connect(ClientInterface stub, String clientPublicKey) {

        return  new ResponseCode( ResponseCode.Codici.R210, ResponseCode.TipoClasse.SERVER,this.serverPublicKey);
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
    public ResponseCode retrieveAccount(String username, String plainPassword, ClientInterface clientStub, String cookie){
        try {
            int accountId=getAccountId(cookie);
            Account account = accountList.getAccountCopy(accountId);
            if(account.getUsername().equals(username)&&account.cmpPassword(plainPassword)){//okay -->setto lo stub
                accountList.setStub(clientStub,accountId);
                return new ResponseCode(ResponseCode.Codici.R220, ResponseCode.TipoClasse.SERVER,"login andato a buon fine");
            }else{
                /*qui non è detto che l'account non esista... potrebbe non essere più associato a quel determinato cookie..
                si potrebbe fare una scansione dell'array alla ricerca dell'account perduto e se trovato  creare un nuovo cookie e inviare un responsecode R100(set cookie)
                 */
                return new ResponseCode(ResponseCode.Codici.R630, ResponseCode.TipoClasse.SERVER,"login fallito:username o password non validi, o cookie non più valido");
            }
        } catch (IllegalBlockSizeException |BadPaddingException | NoSuchAlgorithmException e) {
            if(e instanceof NoSuchAlgorithmException){
                return new ResponseCode(ResponseCode.Codici.R999, ResponseCode.TipoClasse.SERVER,"Internal server error D;");
            }
            else{
                return new ResponseCode(ResponseCode.Codici.R666, ResponseCode.TipoClasse.SERVER,"Formato cookie non valido");
            }

        }

    }

    @Override
    public void subscribe(String cookie, Topic topic)  {

    }

    @Override
    public void unsubscribe(String cookie,Topic topic)  {

    }

    @Override
    public void publish(String cookie) {

    }

    @Override
    public void ping( )  {

    }

    @Override
    public List<Topic> getTopicList()  {

        return null;
    }

    /* ************************************************************************************************************/
    //METODI PRIVATI

    private String getCookie(int accountId) throws BadPaddingException, IllegalBlockSizeException {
        return aesCipher.encrypt(String.valueOf(accountId));
    }

    private int getAccountId(String cookie) throws BadPaddingException, IllegalBlockSizeException {
        return Integer.parseInt(aesCipher.decrypt(cookie));
    }


    private int registerAccount(String userName, String plainPassword, ClientInterface stub, String publicKey,int accountId) throws AccountRegistrationException {
        //sarebbe utile aggiungere un metodo per controllare se l'account esiste già
        //però solleva dei problemi sul testing(localhost non può avere più di un account)->soluzione chiave primaria email associata all'account

        try {
            Account account = new Account(userName, plainPassword, stub, publicKey, accountId);
            return accountList.addAccount(account);
        }catch (Exception exc){
            exc.printStackTrace(System.err);
            throw new AccountRegistrationException("Unable to register new account");
        }
    }

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

    private AccountCollectionInterface createAccountManager(){
        AccountCollectionInterface accManager = null;
        try {
            accManager = new AccountListMonitor(Integer.parseInt(serverSettings.getProperty("maxaccountnumber")));
        }catch (IllegalArgumentException e){
            warningStamp(e, "");
            //System.out.println("[SERVER-WARNING]: "+exc.getClass().getSimpleName()+" --> using default accountmonitor size");
            accManager = new AccountListMonitor();        //Utilizzo del costruttore di default
        }
        return accManager;
    }

    public void start(){
        ServerInterface stub = null;
        Registry r = null;
        try {
            //Importing the security policy and
            System.setProperty("java.security.policy","file:./sec.policy");
            //System.setProperty("java.rmi.server.codebase","file:${workspace_loc}/Server/");

            //Creating and Installing a Security Manager
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            //Creating or import the local regestry
            try {
                r = LocateRegistry.createRegistry(regPort);
            } catch (RemoteException e) {
                r = LocateRegistry.getRegistry(regPort);
            }

            //Making the Remote Object Available to Clients
            stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0); //The 2nd argument specifies which TCP port to use to listen for incoming remote invocation requests . It is common to use the value 0, which specifies the use of an anonymous port. The actual port will then be chosen at runtime by RMI or the underlying operating system.
            r.rebind(serverName, stub);

        }catch (RemoteException e){
            errorStamp(e);
            System.exit(-1);
        }


        this.registry = r;
        this.skeleton = stub;

        /*
       1) Impostare le policy                                                                                       (FATTO)
                System.setProperty("java.security.policy","file:./sec.policy");
                System.setProperty("java.rmi.server.codebase","file:${workspace_loc}/Server/"); o System.setProperty ("java.rmi.server.codebase", "http://130.251.36.239/hello.jar");

           2) Creare se non esiste un SecurityManager                                                               (FATTO)
                if(System.getSecurityManager() == null)
				    System.setSecurityManager(new SecurityManager());

	       3) Importare o creare il registry se non esiste
	            Registry r = null;
	            try {
				    r = LocateRegistry.createRegistry(regPort);
			    } catch (RemoteException e) {
				    r = LocateRegistry.getRegistry(regPort);
			    }
           4) Creare lo stub del server                                                                             (FATTO)
			    ServerInt stubRequest = (ServerInt) UnicastRemoteObject.exportObject(this,0);

           5) Caricare lo stub sul regestry
                r.rebind("REG", stubRequest);


            FINE
         */
    }





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




}
