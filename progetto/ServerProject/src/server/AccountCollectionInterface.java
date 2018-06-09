/**
    Simple distributed forum
    Copyright (C) 2018  Andrea Straforini,Alessandro Caroti,Gabriele Armanino

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

**/
package server;

import client.Client;
import customException.AccountMonitorRuntimeException;
import customException.MaxNumberAccountReached;
import interfaces.ClientInterface;
import utility.Account;

import java.security.NoSuchAlgorithmException;

public interface AccountCollectionInterface {

    /**
     *Aggiunge ad accountList l'istanza account.
     * Se viene aggiunta correttamente (cioè non è già stato raggiunto in precedenza il max numero di account disponibili)
     * account.accountid viene settato con il valore della posizione nella lista dove è stato salvato.(da 0 a MAXACCOUNTNUMBER)
     * @param account deve essere diverso da null
     * @return l'accountId (la posizione nella lista)
     * @throws NullPointerException se viene passata una classe Account non istanziata
     * @throws MaxNumberAccountReached se non ci sono più posti all'interno del monitor
     * @throws AccountMonitorRuntimeException errore irreversibile (non dovrebbe mai succedere)
     */
     int addAccount(Account account)throws MaxNumberAccountReached, AccountMonitorRuntimeException;


    /**
     * Ritorna uno snapshot della classe account nella posizione id della lista degli account
     * @param accountId posizione all'interno dell'array
     * @return null  se non trovato o se index outofbounds
     * @throws NullPointerException  deriva dal costruttore di Account
     *@throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     **/
     Account getAccountCopy(int accountId);


    /**
     * Aggiunge o sovrascrive un account in posizione accountId
     * @return l'Account  eliminato(null se non era presente)
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     * Nota:account.accountId viene settato automaticamente
     */

     Account addAccount(Account account,int accountId);


    /**Elimina e ritorna l'istanza precedente alla posizione "posizione"
     * @param accountId deve essere >=0 AND <MAXNUMBERACCOUNT
     * @return l'istanza di account tolta dalla posizione "accountId"
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     */
     Account removeAccount(int accountId);

    /**
     *Tutti i getter tornano il valore (null o qualcosa di definito) oppure una delle due eccezioni
     * @param accountId la posizione dove è stato salvato
     * @return null or Something
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     * @throws NullPointerException se nella posizione accountId non è salvato alcun account
     */
     String getPublicKey(int accountId);

     byte[] getPassword(int accountId);

     String getUsername(int accountId) ;

     ClientInterface getStub(int accountId);

    /**
     *Tutti i setter tornano il valore sovrascritto (null o qualcosa di definito) oppure una delle due eccezioni
     * @param accountId la posizione dove è salvato l'account da modificare
     * @return null or Something
     * @throws IllegalArgumentException se accountId<0 || accountId>=MAXACCOUNTNUMBER
     * @throws NullPointerException se nella posizione accountId non è salvato alcun account
     */

     String setPublicKey(String clientPublicKey,int accountId);

     byte[] setPassword(String plainPassword,int accountId)throws NoSuchAlgorithmException;

     String setUsername(String username,int acountId);

     ClientInterface setStub(ClientInterface clientStub,int accountId);

     /**semplici getter**/
     int getNumberOfAccount();

     int getMAXACCOUNTNUMBER();
}
