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
    private AccountCollectionInterface accountList;         //monitor della lista contente tutti gli account salvati
    private Properties serverSettings=new Properties();     //setting del server
    private ConcurrentHashMap<byte[],Integer> topicCookieAssociation;   //aka  hashMap contenente le associazioni topic->accountId
    private AES aesCipher;
    private String serverPublicKey;
    private String serverPrivateKey;

    /*rmi fields*/
    private Registry registry;
    private ServerInterface skeleton;

    /**Costruttore
     *carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     *  se il file di config non viene trovato
     */
    /*TODO creare le chiavi pubbliche eccetera e settarle nei fields*/

    public Server() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, AlreadyBoundException, RemoteException, UnknownHostException {
        try {
            FileInputStream in = new FileInputStream("config.serverSettings");
            serverSettings.load(in);
            in.close();
            accountList=new AccountListMonitor(Integer.parseInt(serverSettings.getProperty("maxaccountnumber")));
                    //TODO here!!!!!!!!!!!!!!!!!!!!!!
            serverPrivateKey="privatekeyservertobeimplmented";
            serverPublicKey="publickeyservertobeimplmented";


        }catch (IOException exc){
            this.accountList=new AccountListMonitor();//usa il default
            System.out.println("WARNING "+exc.getClass().getSimpleName()+"-->using default accountmonitor size");
        }

        this.skeleton = (ServerInterface) UnicastRemoteObject.exportObject(this, 1099);//TODO CHANGE PORT HERE?
        this.registry= LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress());
        try {
            this.registry.rebind("ServerInterface", skeleton);
        }catch(NoSuchObjectException e){
            System.out.println("WARNING "+e.getClass().getSimpleName()+"-->forzo l'avvio del registry");
            this.registry=LocateRegistry.createRegistry(1099);
            this.registry= LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress());
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

    /* ********************************************************************************************************** **/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */




    /* ************************************************************************************************************/
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
            return responseCode=new ResponseCode(ResponseCode.Codici.R610, ResponseCode.TipoClasse.SERVER,"Registrazione account fallita");
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




}
