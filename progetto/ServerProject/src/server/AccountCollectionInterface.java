package server;

import customException.MaxNumberAccountReached;
import interfaces.ClientInterface;
import utility.Account;

public interface AccountCollectionInterface {

    /**
     *Aggiunge ad accountList l'istanza account.
     * Se viene aggiunta correttamente (cioè non viene raggiunto il max numero di account disponibili)
     * account.accountid viene settato con il valore della posizione della lista dove è stato salvato.
     * @param account diverso da null
     * @return l'accountId (la posizione nella lista)
     */
     int addAccount(Account account)throws MaxNumberAccountReached;


    /**
     * Ritorna uno snapshot della classe account nella posizione id della lista degli account
     * @param accountId posizione all'interno dell'array
     * @return null  se non trovato o se index outofbounds
     * @throws NullPointerException  deriva dal costruttore di Account
     * @throws IllegalArgumentException deriva dal costruttore di Account
     */
     Account getAccountCopy(int accountId);


    /**
     * Aggiunge o sovrascrive un account in posizione posizione
     * @return true se aggiunto correttamente ,false se index outofbounds
     * Nota:account.accountId viene settato automaticamente
     */

     boolean addAccount(Account account,int accountId);


    /**Elimina e ritorna l'istanza precedente alla posizione "posizione"
     * @param accountId deve essere >=0 AND <MAXNUMBERACCOUNT
     * @return l'istanza di account tolta dalla posizione "posizione"
     */
     Account removeAccount(int accountId);

     String getPublicKey(int accountId);

    /**
     * @return torna l'hash salvato in account
     */
     byte[] getPassword(int accountId);

     String getUsername(int accountId) ;

     ClientInterface getStub(int accountId);

     int getLength();

    public int getMaxLength();
}
