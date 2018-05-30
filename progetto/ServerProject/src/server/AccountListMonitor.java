package server;

import interfaces.ClientInterface;
import java.util.concurrent.locks.*;
import customException.*;
import utility.Account;

/**
 * Implementazione con multiple reader single writer lock
 */
public class AccountListMonitor implements AccountCollectionInterface {
    private  int MAXACCOUNTNUMBER;
    private final int MAXACCOUNTNUMBERDEFAULT=300;
    private  Account[] accountList;
    private int length=0;
    private ReentrantReadWriteLock listLock=new ReentrantReadWriteLock();


    /**
     * @param maxAccountNumber il numero massimo di account
     * @throws IllegalArgumentException se MAXACCOUNTNUMBER <=0
     */
    public AccountListMonitor(int maxAccountNumber) throws  IllegalArgumentException
    {
        if(MAXACCOUNTNUMBER<=0){
            throw new IllegalArgumentException("maxaccountnumber<=0");
        }
        this.MAXACCOUNTNUMBER=maxAccountNumber;
        this.accountList=new Account[MAXACCOUNTNUMBER];
    }

    /**
     * Setta il numero massimo di account con il valore di dafault
     */
     public  AccountListMonitor(){

         this.MAXACCOUNTNUMBER=this.MAXACCOUNTNUMBERDEFAULT;
         this.accountList=new Account[MAXACCOUNTNUMBER];
    }

    /**
     *Aggiunge ad accountList l'istanza account.
     * Se viene aggiunta correttamente (cioè non viene raggiunto il max numero di account disponibili)
     * account.accountid viene settato con il valore della posizione della lista dove è stato salvato.
     * @param account diverso da null
     * @return l'accountId (la posizione nella lista)
     * @throws NullPointerException se account==null
     * @throws MaxNumberAccountReached reached max number of account
     */
    public int addAccount(Account account) throws NullPointerException,MaxNumberAccountReached
    {
        int posizione;
        if(account==null)
        {
            throw new NullPointerException("account==null");
        }

        this.listLock.writeLock().lock();
        try{
            if(this.length==MAXACCOUNTNUMBER){
                throw new MaxNumberAccountReached();
            }
            accountList[this.length]=account;
            account.setAccountId(this.length);
            posizione=this.length++;
        }finally{
            listLock.writeLock().unlock();
        }

        return posizione;
    }


    /**
     * Ritorna uno snapshot della classe account nella posizione id della lista degli account
     * @param accountId posizione all'interno dell'array
     * @return null  se non trovato o se index outofbounds
     * @throws NullPointerException  deriva dal costruttore di Account
     * @throws IllegalArgumentException deriva dal costruttore di Account
     */
    public Account getAccountCopy(int accountId){
        if(accountId>=this.getLength()){
            return null;
        }
        Account snapShot;
        this.listLock.readLock().lock();
        Account curr;
        try {
            curr = this.accountList[accountId];
            if(curr==null){
                return null;
            }
            snapShot = new
                    Account(curr.getUsername(), curr.getPassword(), curr.getStub(), curr.getPublicKey(), curr.getAccountId());
            return snapShot;
        }
        finally {
            listLock.readLock().unlock();
        }
    }

    /**
     * Aggiunge o sovrascrive un account in posizione posizione
     * Nota:account.accountId viene settato automaticamente
     * @param account l'istanza della classe Account
     * @param accountId l'identificativo dell'account
     * @throws NullPointerException se account==null
     * @throws IndexOutOfBoundsException se accountId>=MAXNUMBERACCOUNT
     * @return true se aggiunto correttamente ,false se index outofbounds
     */

    public boolean addAccount(Account account,int accountId)throws NullPointerException,IndexOutOfBoundsException
    {
        if(account==null)
        {
            throw new NullPointerException("account==null");
        }
        if(accountId>=MAXACCOUNTNUMBER){
            throw new IndexOutOfBoundsException("accountId>=MAXACCOUNTNUMBER");
        }

        listLock.writeLock().lock();
            if(accountId>this.length){
                listLock.writeLock().unlock();
                return false;
            }
            account.setAccountId(accountId);
            accountList[accountId]=account;
            if(accountId==this.length)
            {this.length++;}
        listLock.writeLock().unlock();

        return true;
    }

    /**Elimina e ritorna l'istanza precedente alla posizione "posizione"
     * @param accountId deve essere >=0 AND <MAXNUMBERACCOUNT AND <length
     * @return l'istanza di account tolta dalla posizione "posizione" oppure null se accountid >=length
     * @throws IndexOutOfBoundsException se posizione>=MAXNUMBERACCOUNT
     */
    public Account removeAccount(int accountId) throws IndexOutOfBoundsException
    {
        Account account;
        if(accountId>=MAXACCOUNTNUMBER){
            throw new IndexOutOfBoundsException("accountId>=MAXACCOUNTNUMBER");
        }


        listLock.writeLock().lock();
            if(accountId>this.length){
                listLock.writeLock().unlock();
                return null;
            }
            account=accountList[accountId];
            accountList[accountId]=null;

        listLock.writeLock().unlock();

        return account;
    }

    /*TODO
        1)se la remove account non fa shiftare l'array allora in addaccount(account)
            bisogna fare la scansione dell'array per trovare una cella ==null



     */

    /**
     * Torna la chiave pubblica dell'account o null
     * @param accountId l'identificativo dell'account
     * @return null se accountId >=length
     */

    public String getPublicKey(int accountId) {
        if(accountId>=this.getLength()){
            return null;
        }
        Account curr;
        String pk;
        listLock.readLock().lock();
        curr=accountList[accountId];
        if(curr==null){
            listLock.readLock().unlock();
            throw new NullPointerException("non esiste un account alla posizione accountId");
        }
        pk=curr.getPublicKey();
        listLock.readLock().unlock();
        return pk;
    }

    /**
     * Torna la password hashata o null
     * @param accountId
     * @return null se accountid>=length
     */
    public byte[] getPassword(int accountId) {
        if(accountId>=this.getLength()){
            return null;
        }
        Account curr;
        byte[] passw;
        listLock.readLock().lock();
        curr=accountList[accountId];
        if(curr==null){
            listLock.readLock().unlock();
            throw new NullPointerException("non esiste un account alla posizione accountId");
        }
        passw=curr.getPassword();
        listLock.readLock().unlock();
        return passw;
    }

    public String getUsername(int accountId) {
        if(accountId>=this.getLength()){
            return null;
        }
        Account curr;
        String username;
        listLock.readLock().lock();
        curr=accountList[accountId];
        if(curr==null){
            listLock.readLock().unlock();
            throw new NullPointerException("non esiste un account alla posizione accountId");
        }
        username=curr.getUsername();
        listLock.readLock().unlock();
        return username;
    }

    public ClientInterface getStub(int accountId) {
        if(accountId>=this.getLength()){
            return null;
        }
        Account curr;
        ClientInterface stub;
        listLock.readLock().lock();
        curr=accountList[accountId];
        if(curr==null){
            listLock.readLock().unlock();
            throw new NullPointerException("non esiste un account alla posizione accountId");
        }
        stub=curr.getStub();
        listLock.readLock().unlock();
        return stub;
    }

    public int getLength() {
        int l;
        listLock.readLock().lock();
         l=this.length;
         listLock.readLock().unlock();
         return l;
    }

    public int getMaxLength()
    {
        return this.MAXACCOUNTNUMBER;
    }

}
