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
    private final int MAXACCOUNTNUMBERDEFAULT=30;
    private  Account[] accountList=new Account[MAXACCOUNTNUMBER];
    private int length=0;
    private ReentrantReadWriteLock listLock=new ReentrantReadWriteLock();

    /**
     * @param maxAccountNumber il numero massimo di account
     * @throws IllegalArgumentException se MAXACCOUNTNUMBER <=0
     */
     AccountListMonitor(int maxAccountNumber) throws  IllegalArgumentException
    {
        if(MAXACCOUNTNUMBER<=0){
            throw new IllegalArgumentException("maxaccountnumber<=0");
        }
        this.MAXACCOUNTNUMBER=maxAccountNumber;
    }

    /**
     * Setta il numero massimo di account con il valore di dafault
     */
     AccountListMonitor(){
        this.MAXACCOUNTNUMBER=this.MAXACCOUNTNUMBERDEFAULT;
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
            posizione=0;
            while(posizione<MAXACCOUNTNUMBER){
                if(accountList[posizione]==null){
                    accountList[posizione]=account;
                    account.setAccountId(posizione);
                    break;
                }
                posizione++;
            }
            if(posizione==MAXACCOUNTNUMBER){
                throw new MaxNumberAccountReached();
            }
            this.length++;
        }finally{
            listLock.writeLock().unlock();
        }

        return posizione;
    }


    /**
     * Ritorna uno snapshot della classe account nella posizione id della lista degli account
     * @param accountId posizione all'interno dell'array
     * @return null  se non trovato
     * @throws NullPointerException  deriva dal costruttore di Account
     * @throws IllegalArgumentException deriva dal costruttore di Account
     */
    public Account getAccountCopy(int accountId){
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
     */

    public void addAccount(Account account,int accountId)throws NullPointerException,IndexOutOfBoundsException
    {
        if(account==null)
        {
            throw new NullPointerException("account==null");
        }
        if(accountId>=MAXACCOUNTNUMBER){
            throw new IndexOutOfBoundsException("accountId>=MAXACCOUNTNUMBER");
        }
        account.setAccountId(accountId);
        listLock.writeLock().lock();
        accountList[accountId]=account;
        this.length++;
        listLock.writeLock().unlock();
    }

    /**Elimina e ritorna l'istanza precedente alla posizione "posizione"
     * @param accountId deve essere >=0 AND <MAXNUMBERACCOUNT
     * @return l'istanza di account tolta dalla posizione "posizione"
     * @throws IndexOutOfBoundsException se posizione>=MAXNUMBERACCOUNT
     */
    public Account removeAccount(int accountId) throws IndexOutOfBoundsException
    {
        Account account;
        if(accountId>=MAXACCOUNTNUMBER){
            throw new IndexOutOfBoundsException("accountId>=MAXACCOUNTNUMBER");
        }
        listLock.writeLock().lock();
        account=accountList[accountId];
        accountList[accountId]=null;
        this.length--;
        listLock.writeLock().unlock();
        return account;
    }


    public String getPublicKey(int accountId) {
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

    public byte[] getPassword(int accountId) {
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

}
