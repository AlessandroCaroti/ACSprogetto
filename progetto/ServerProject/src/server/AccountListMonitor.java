package server;

import interfaces.ClientInterface;
import java.util.concurrent.locks.*;
import customException.*;
import utility.Account;

import javax.management.RuntimeErrorException;

/**
 * Implementazione con multiple reader single writer lock
 */
public class AccountListMonitor implements AccountCollectionInterface {
    private  int MAXACCOUNTNUMBER;
    private final int MAXACCOUNTNUMBERDEFAULT=300;
    private  Account[] accountList;
    private int length=0;
    private ReentrantReadWriteLock listLock=new ReentrantReadWriteLock();
    private int lastFreeposition=-1;//funziona come una cache

    public AccountListMonitor(int maxAccountNumber) throws  IllegalArgumentException
    {
        if(MAXACCOUNTNUMBER<=0){
            throw new IllegalArgumentException("maxaccountnumber<=0");
        }
        this.MAXACCOUNTNUMBER=maxAccountNumber;
        this.accountList=new Account[MAXACCOUNTNUMBER];
    }


     public  AccountListMonitor(){

         this.MAXACCOUNTNUMBER=this.MAXACCOUNTNUMBERDEFAULT;
         this.accountList=new Account[MAXACCOUNTNUMBER];
    }


    public int addAccount(Account account) throws NullPointerException,MaxNumberAccountReached,AccountMonitorRuntimeException
    {
        int posizione;
        if(account==null)
        {
            throw new NullPointerException("account==null");
        }
        if(this.getNumberOfAccount()>=MAXACCOUNTNUMBER){
            throw new MaxNumberAccountReached();
        }


        this.listLock.writeLock().lock();
        try{

            if(lastFreeposition!=-1){//cache funzionante
                accountList[lastFreeposition]=account;
                account.setAccountId(lastFreeposition);
                posizione=lastFreeposition;//System.err.println(" cache!");
                lastFreeposition=-1;
                return posizione;
            }else{
               for (int i=0;i<this.MAXACCOUNTNUMBER;i++){
                   if(accountList[i]==null){
                       accountList[i]=account;
                       account.setAccountId(i);
                       return i;
                   }
               }
            }
        }finally{
            this.length++;
            listLock.writeLock().unlock();
        }
        throw new AccountMonitorRuntimeException("ERRORE:addAccount");//Non dovrebbe mai essere sollevata :D speremmu!
    }

    public Account getAccountCopy(int accountId){

        Account snapShot;
        Account curr;

        if(accountId>=this.MAXACCOUNTNUMBER||accountId<0){
            throw new IllegalArgumentException("accountId>MAXACCOUNTNUMBER || accountId<0");
        }

        this.listLock.readLock().lock();
        try {
            curr = accountList[accountId];
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

    public Account addAccount(Account account,int accountId)
    {
        if(accountId>=this.MAXACCOUNTNUMBER||accountId<0){
            throw new IllegalArgumentException("accountId>MAXACCOUNTNUMBER || accountId<0");
        }
        Account prev;
        this.listLock.writeLock().lock();
        try{
            prev=accountList[accountId];

            //gestione length
            if(prev!=null&&account==null){
                this.length--;
            }else if(prev==null && account!=null){
                this.length++;
            }
            if(account==null){
                lastFreeposition=accountId;
            }else{
                account.setAccountId(accountId);
            }
            accountList[accountId]=account;
        }finally {
            this.listLock.writeLock().unlock();
        }
        return prev;
    }


    public Account removeAccount(int accountId)
    {
        return this.addAccount(null,accountId);
    }

    public String getPublicKey(int accountId) {

        if(accountId>=this.MAXACCOUNTNUMBER||accountId<0){
            throw new IllegalArgumentException("accountId>MAXACCOUNTNUMBER || accountId<0");
        }

        listLock.readLock().lock();
        try {
            return accountList[accountId].getPublicKey();
        }
        finally {
            this.listLock.readLock().unlock();
        }
    }


    public byte[] getPassword(int accountId) {

        if(accountId>=this.MAXACCOUNTNUMBER||accountId<0){
            throw new IllegalArgumentException("accountId>MAXACCOUNTNUMBER || accountId<0");
        }

        listLock.readLock().lock();
        try {
            return accountList[accountId].getPassword();
        }
        finally {
            this.listLock.readLock().unlock();
        }
    }

    public String getUsername(int accountId) {
        if(accountId>=this.MAXACCOUNTNUMBER||accountId<0){
            throw new IllegalArgumentException("accountId>MAXACCOUNTNUMBER || accountId<0");
        }
        listLock.readLock().lock();
        try {
            return accountList[accountId].getUsername();

        }
        finally {
            this.listLock.readLock().unlock();
        }
    }

    public ClientInterface getStub(int accountId) {
        if(accountId>=this.MAXACCOUNTNUMBER||accountId<0){
            throw new IllegalArgumentException("accountId>MAXACCOUNTNUMBER || accountId<0");
        }
        listLock.readLock().lock();
        try {
            return accountList[accountId].getStub();

        }
        finally {
            this.listLock.readLock().unlock();
        }
    }

    public int getNumberOfAccount() {
        int l;
        listLock.readLock().lock();
         l=this.length;
         listLock.readLock().unlock();
         return l;
    }

    public int getMAXACCOUNTNUMBER()
    {
        return this.MAXACCOUNTNUMBER;
    }

}
