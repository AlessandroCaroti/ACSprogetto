/*
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

*/
package account;

import customException.AccountMonitorRuntimeException;
import customException.MaxNumberAccountReached;
import interfaces.ClientInterface;
import utility.Account;

import java.security.NoSuchAlgorithmException;

public interface AccountCollectionInterface {

    /**
     * Aggiunge ad accountList l'istanza account.
     * Se viene aggiunta correttamente (cioè non è già stato raggiunto in precedenza il max numero di account disponibili)
     * account.accountid viene settato con il valore della posizione nella lista dove è stato salvato.(da 0 a MAXACCOUNTNUMBER)
     *
     * @param account deve essere diverso da null
     * @return l'accountId (la posizione nella lista)
     * @throws NullPointerException           se viene passata una classe Account non istanziata
     * @throws MaxNumberAccountReached        se non ci sono più posti all'interno del monitor
     * @throws AccountMonitorRuntimeException errore irreversibile (non dovrebbe mai succedere)
     */
    int addAccount(Account account) throws MaxNumberAccountReached, AccountMonitorRuntimeException;


    /**
     * Ritorna uno snapshot della classe account nella posizione id della lista degli account
     *
     * @param accountId posizione all'interno dell'array
     * @return null  se non trovato
     * @throws NullPointerException     deriva dal costruttore di Account
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     **/
    Account getAccountCopy(int accountId);

    /**
     * Ritorna uno snapshot della classe account con username uguale a quello passato
     *
     * @param userName l'username da cercare
     * @return null se non esiste un account con quel username
     * @throws IllegalArgumentException se l'username passato è null
     */
    Account getAccountCopyUsername(String userName);

    /**
     * Ritorna uno snapshot della classe account con email uguale a quello passato
     *
     * @param email l'email da cercare
     * @return null se non esiste un account con quel username
     * @throws IllegalArgumentException se l'email passato è null
     */
    Account getAccountCopyEmail(String email);

    /**
     * Aggiunge o sovrascrive un account in posizione accountId
     *
     * @return l'Account  eliminato(null se non era presente)
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     * @throws NullPointerException     se l'account passato è null
     *                                  Nota:account.accountId viene settato automaticamente
     */

    Account addAccount(Account account, int accountId);


    /**
     * Elimina e ritorna l'istanza precedente alla posizione accountId
     *
     * @param accountId deve essere >=0 AND <MAXNUMBERACCOUNT
     * @return l'istanza di account tolta dalla posizione "accountId" (può essere null)
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     */
    Account removeAccount(int accountId);

    /**
     * Elimina e ritorna l'istanza precedente alla posizione accountId, controllando però che la mail salvata nell'account
     * corrisponda a quella passata (equalsIgnoreCase). Se non corrisponde torna null
     *
     * @param accountId la posizione dell'accoutn da eliminare
     * @param email     l'email da controllare
     * @return null se non corrisponde o se non trovata, l'istanza account eliminata altrimenti
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     * @throws NullPointerException     se email==null
     */
    Account removeAccountCheckEmail(int accountId, String email);

    /**
     * @param account l'account da aggiungere con già i suoi fields settati
     * @return l'account id se non era presente; Torna -1 se la mail è già presente  ,  -2 se l'username è già presente
     * L'esistenza dell'username uguale a quello passato ha "la precedenza" sulla mail
     * @throws NullPointerException           se i fields email o username di account sono settati a null
     * @throws MaxNumberAccountReached        se non ci sono più posti disponibili
     * @throws IllegalArgumentException       se viene passato un reference null
     * @throws AccountMonitorRuntimeException errore irreversibile (non dovrebbe mai succedere)
     */
    int putIfAbsentEmailUsername(Account account) throws NullPointerException, MaxNumberAccountReached, IllegalArgumentException, AccountMonitorRuntimeException;

    /**
     * @param account l'account da aggiungere con già i suoi fields settati
     * @return l'account id se non era presente; Torna  -2 se l'username è già presente
     * @throws NullPointerException           se il field  username di account è settato a null
     * @throws MaxNumberAccountReached        se non ci sono più posti disponibili
     * @throws IllegalArgumentException       se viene passato un reference null
     * @throws AccountMonitorRuntimeException errore irreversibile (non dovrebbe mai succedere)
     */
    int putIfAbsentUsername(Account account) throws NullPointerException, MaxNumberAccountReached, IllegalArgumentException, AccountMonitorRuntimeException;


    /**
     * Controlla se esiste già un account con email OR username  (importante l'OR e non AND)
     *
     * @param email    l'email da cercare
     * @param username l'username da cercare
     * @return una copia dell'account che ha email OR password uguale a quelle passate,se l'account non esiste torna null
     * @throws NullPointerException se email AND username ==  null
     */
    Account isMember(String email, String username) throws IllegalArgumentException;


    /**
     * Tutti i getter tornano il valore richiesto associato all'account in posizione accountId
     * Nota:torna uno snapshot
     *
     * @param accountId la posizione dove è stato salvato
     * @return il valore richiesto
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     * @throws NullPointerException     se nella posizione accountId non è salvato alcun account
     */
    String getPublicKey(int accountId);

    byte[] getPassword(int accountId);

    String getUsername(int accountId);

    ClientInterface getStub(int accountId);//TODO guardare implementazione metodo nella classe AccountListMonitor (possibile bug)

    String getEmail(int accountId);

    String[] getTopicSubscribed(int accountId);

    /**
     * Tutti i setter tornano il valore sovrascritto (null o qualcosa di definito) oppure una delle due eccezioni
     *
     * @param accountId la posizione dove è salvato l'account da modificare
     * @return null or Something
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     * @throws NullPointerException     se nella posizione accountId non è salvato alcun account
     */

    String setPublicKey(String clientPublicKey, int accountId);

    byte[] setPassword(String plainPassword, int accountId) throws NoSuchAlgorithmException;

    String setUsername(String username, int accountId);

    ClientInterface setStub(ClientInterface clientStub, int accountId);

    String setEmail(String email, int accountId);

    boolean addTopic(String topicName, int accountId);

    boolean removeTopic(String topicName, int accountId);

    /**
     * semplici getter
     **/
    int getNumberOfAccount();

    int getMAXACCOUNTNUMBER();
}
