package server;

import customException.AccountMonitorRuntimeException;
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
     int addAccount(Account account)throws MaxNumberAccountReached, AccountMonitorRuntimeException;


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
     * @return Account l'account eliminato(null se non era presente)
     * Nota:account.accountId viene settato automaticamente
     */

     Account addAccount(Account account,int accountId);


    /**Elimina e ritorna l'istanza precedente alla posizione "posizione"
     * @param accountId deve essere >=0 AND <MAXNUMBERACCOUNT
     * @return l'istanza di account tolta dalla posizione "posizione"
     */
     Account removeAccount(int accountId);

    /**
     *Tutti i getter tornano il valore (null o qualcosa di definito) oppure una delle due eccezioni
     * @param accountId la posizione dove è stato salvato
     * @return null or Something
     * @throws ArrayIndexOutOfBoundsException se viene richiesto un accountId>=MAxnumberofaccount
     * @throws NullPointerException se nella posizione accountId non è salvato alcun account
     */
     String getPublicKey(int accountId);

     byte[] getPassword(int accountId);

     String getUsername(int accountId) ;

     ClientInterface getStub(int accountId);

     int getNumberOfAccount();

     int getMAXACCOUNTNUMBER();
}
