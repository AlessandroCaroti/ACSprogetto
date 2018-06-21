/*
 * This file is part of ACSprogetto.
 * <p>
 * ACSprogetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * ACSprogetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with ACSprogetto.  If not, see <http://www.gnu.org/licenses/>.
 **/

package account;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.locks.*;
import interfaces.ClientInterface;
import customException.*;
import utility.Account;


/* *
 * Implementazione con multiple reader single writer lock
 */
public class AccountListMonitor implements AccountCollectionInterface {

    final private int MAXACCOUNTNUMBERDEFAULT = 300;
    final private int MAXACCOUNTNUMBER;

    private Account[] accountList;
    private int length = 0;
    private int lastFreeposition = -1;//funziona come una cache

    private ReentrantReadWriteLock listLock = new ReentrantReadWriteLock();

    /* ****************************************************************************************************/
    //COSTRUTTORI
    public AccountListMonitor(int maxAccountNumber) throws IllegalArgumentException {
        if (maxAccountNumber <= 0) {
            throw new IllegalArgumentException("Class: AccountListMonitor - Error: maxAccountNumber <= 0");
        }
        this.MAXACCOUNTNUMBER = maxAccountNumber;
        this.accountList = new Account[MAXACCOUNTNUMBER];
    }

    public AccountListMonitor() {
        this.MAXACCOUNTNUMBER = this.MAXACCOUNTNUMBERDEFAULT;
        this.accountList = new Account[MAXACCOUNTNUMBER];
    }


    /* ****************************************************************************************************/
    //METODI MODIFICATORI
    public int addAccount(Account account) throws NullPointerException, MaxNumberAccountReached, AccountMonitorRuntimeException {
        int posizione;
        if (account == null) {
            throw new NullPointerException("account==null");
        }
        if (this.getNumberOfAccount() >= MAXACCOUNTNUMBER) {
            throw new MaxNumberAccountReached();
        }


        listLock.writeLock().lock();
        try {

            if (lastFreeposition != -1) {//cache funzionante
                accountList[lastFreeposition] = account;
                account.setAccountId(lastFreeposition);
                posizione = lastFreeposition;//System.err.println(" cache!");
                lastFreeposition = -1;
                return posizione;
            } else {
                for (int i = 0; i < this.MAXACCOUNTNUMBER; i++) {
                    if (accountList[i] == null) {
                        accountList[i] = account;
                        account.setAccountId(i);
                        return i;
                    }
                }
            }
        } finally {
            this.length++;
            listLock.writeLock().unlock();
        }
        throw new AccountMonitorRuntimeException("ERRORE:addAccount");//Non dovrebbe mai essere sollevata :D speremmu!
    }

    public Account addAccount(Account account, int accountId) {
        testRange(accountId);
        Account prev;

        listLock.writeLock().lock();
        try {
            prev = accountList[accountId];

            //gestione length
            if (prev != null && account == null) {
                this.length--;
            } else if (prev == null && account != null) {
                this.length++;
            }
            if (account == null) {
                lastFreeposition = accountId;
            } else {
                account.setAccountId(accountId);
            }
            accountList[accountId] = account;
        } finally {
            this.listLock.writeLock().unlock();
        }
        return prev;
    }


    public Account removeAccount(int accountId) {
        testRange(accountId);

        Account toRemove;
        listLock.writeLock().lock();
        try {
            toRemove = accountList[accountId];
            if (toRemove != null) {
                this.length--;
                accountList[accountId] = null;
                lastFreeposition = accountId;
            }
        } finally {
            this.listLock.writeLock().unlock();
        }
        return toRemove;
    }

    public int putIfAbsentEmailUsername(Account account) throws NullPointerException, MaxNumberAccountReached, IllegalArgumentException, AccountMonitorRuntimeException {

        if(account==null)throw new IllegalArgumentException("account==null");
        if (this.getNumberOfAccount() >= MAXACCOUNTNUMBER) {
            throw new MaxNumberAccountReached();
        }
        String email=account.getEmail();
        String username=account.getUsername();
        if(email==null||username==null)throw new NullPointerException("email or username==null");

        this.listLock.writeLock().lock();
        try {
            for (int i = 0; i < this.length; i++) {
                if (accountList[i] != null) {
                    if (email.equalsIgnoreCase(accountList[i].getEmail())) {
                        return -1;
                    }
                    if(username.equals(accountList[i].getUsername())){
                        return -2;
                    }
                }
            }//se finisce il for vuol dire che non sono presenti account con quella email o quella password
            return this.addAccount(account);
        }finally{
            listLock.writeLock().unlock();
        }
    }


    /* ****************************************************************************************************/
    //METODI GETTER


    public Account isMember(String email,String username) throws IllegalArgumentException {
        if(email==null||username==null){throw new IllegalArgumentException("email==null || username==null");}
        String[] coppia;

        listLock.readLock().lock();
        try {
            for (int i = 0; i < this.length; i++) {
                coppia = this.getEmailAndUsername(i);
                if (email.equalsIgnoreCase(coppia[0]) || username.equalsIgnoreCase(coppia[1])) {
                    return this.getAccountCopy(i);
                }
            }
            return null;
        }finally{
            listLock.readLock().unlock();
        }
    }



    public Account getAccountCopy(int accountId) {
        testRange(accountId);

        Account snapShot;
        Account curr;

        this.listLock.readLock().lock();
        try {
            curr = accountList[accountId];
            if (curr == null) {
                return null;
            }
            snapShot = curr.copy();
            return snapShot;
        } finally {
            listLock.readLock().unlock();
        }
    }


    public String getPublicKey(int accountId) {

        testRange(accountId);
        listLock.readLock().lock();
        try {

            return new String(accountList[accountId].getPublicKey());//è uno snapshot non è ridondante!
        } finally {
            this.listLock.readLock().unlock();
        }
    }

    public byte[] getPassword(int accountId) {
        testRange(accountId);

        listLock.readLock().lock();
        try {
            byte[] returnValue;
            returnValue= Arrays.copyOf(accountList[accountId].getPassword(),accountList[accountId].getPassword().length);//snapshot
            return returnValue;
        } finally {
            this.listLock.readLock().unlock();
        }
    }

    public String getUsername(int accountId) {
        testRange(accountId);

        listLock.readLock().lock();
        try {
            return new String(accountList[accountId].getUsername());//è uno snapshot non è ridondante!
        } finally {
            this.listLock.readLock().unlock();
        }
    }

    public ClientInterface getStub(int accountId) {
        testRange(accountId);

        listLock.readLock().lock();
        try {
            //ClientInterface returnValue=new
            return  accountList[accountId].getStub();//TODO come faccio a ritornare uno snapshot di clientInterface?
        } finally {
            this.listLock.readLock().unlock();
        }
    }

    public String getEmail(int accountId){
        testRange(accountId);

        listLock.readLock().lock();
        try{
            return new String(accountList[accountId].getEmail());//è uno snapshot non è ridondante!
        }finally{
            listLock.readLock().unlock();
        }
    }


    public int getNumberOfAccount() {
        int l;
        listLock.readLock().lock();
        l = this.length;
        listLock.readLock().unlock();
        return l;
    }

    public int getMAXACCOUNTNUMBER() {
        return this.MAXACCOUNTNUMBER;
    }


    /* ****************************************************************************************************/
    //METODI SETTER
    public String setPublicKey(String clientPublicKey, int accountId) {
        testRange(accountId);

        listLock.writeLock().lock();
        try {
            String prev = accountList[accountId].getPublicKey();
            accountList[accountId].setPublicKey(clientPublicKey);
            return prev;
        } finally {
            this.listLock.writeLock().unlock();
        }
    }

    public byte[] setPassword(String plainPassword, int accountId) throws NoSuchAlgorithmException {
        testRange(accountId);

        listLock.writeLock().lock();
        try {
            byte[] prev = accountList[accountId].getPassword();
            accountList[accountId].encryptAndSetPassword(plainPassword);
            return prev;
        } finally {
            this.listLock.writeLock().unlock();
        }
    }


    public String setUsername(String username, int accountId) {
        testRange(accountId);

        listLock.writeLock().lock();
        try {
            String prev = accountList[accountId].getUsername();
            accountList[accountId].setUsername(username);
            return prev;
        } finally {
            this.listLock.writeLock().unlock();
        }
    }

    public ClientInterface setStub(ClientInterface clientStub, int accountId) {
        testRange(accountId);

        listLock.writeLock().lock();
        try {
            ClientInterface prev = accountList[accountId].getStub();
            accountList[accountId].setStub(clientStub);
            return prev;
        } finally {
            this.listLock.writeLock().unlock();
        }
    }

    public String setEmail(String email,int accountId){
        testRange(accountId);
        listLock.writeLock().lock();
        try{
            String oldEmail=accountList[accountId].getEmail();
            accountList[accountId].setEmail(email);
            return oldEmail;
        }finally{
            listLock.writeLock().unlock();
        }
    }

    /* ****************************************************************************************************/
    //METODI PRIVATI
    private void testRange(int n) {
        if (n >= this.MAXACCOUNTNUMBER || n < 0) {
            throw new IllegalArgumentException("accountId>MAXACCOUNTNUMBER || accountId<0");
        }
    }

    private String[] getEmailAndUsername(int accountId){
        String[] coppia=new String[2];
        testRange(accountId);

        listLock.readLock().lock();//reentrant lock
        try{
            coppia[0]= this.getEmail(accountId);
            coppia[1]= this.getUsername(accountId);
            return coppia;
        }finally{
            listLock.readLock().unlock();
        }

    }





}

