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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
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
    private AccountCollectionInterface accountList;         //monitor della lista contente tutti gli account salvati
    private Properties serverSettings=new Properties();     //setting del server
    private ConcurrentHashMap<byte[],Integer> topicCookieAssociation;   //aka  hashMap contenente le associazioni topic->accountId
    private AES aesCipher;
    private String serverPublicKey;
    private String serverPrivateKey;



    /**Costruttore
     *carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     *  se il file di config non viene trovato
     */
    Server() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException {
        try {
            FileInputStream in = new FileInputStream("config.serverSettings");
            serverSettings.load(in);
            in.close();
            this.accountList=new AccountListMonitor(Integer.parseInt(serverSettings.getProperty("maxaccountnumber")));
                    //TODO aggiungere i costruttori con i loro setting

        }catch (IOException exc){
            this.accountList=new AccountListMonitor();//usa il default
        }
        try {
            aesCipher = new AES("RandomInitVectol");
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




        return 0;
    }

    /* ********************************************************************************************************** **/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */




    /* ************************************************************************************************************/
    //METODI REMOTI

    @Override
    public ResponseCode register(String userName, String plainPassword, ClientInterface stub, String publicKey) throws RemoteException {
        String cookie;
        ResponseCode responseCode;
        try{
           cookie=getCookie(registerAccount(userName, plainPassword, stub, publicKey, 0/*lo setta automaticamente dopo*/));
           return responseCode=new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER,cookie);

       } catch (Exception exc){
            return responseCode=new ResponseCode(ResponseCode.Codici.R610, ResponseCode.TipoClasse.SERVER,"Registrazione account fallita");
        }
    }

    @Override
    public ResponseCode anonymousRegister(ClientInterface stub, String publicKey) throws RemoteException {
        return register("AnonymousAccount","",stub,publicKey);
    }

    @Override
    public ResponseCode connect(ClientInterface stub,String clientPublicKey) {

        return  new ResponseCode(ResponseCode.Codici.R210, ResponseCode.TipoClasse.SERVER,this.serverPublicKey);
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


}
