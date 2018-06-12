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
package utility;


import interfaces.ClientInterface;

import java.security.NoSuchAlgorithmException;
import static utility.hashFunctions.compareHashandString;
import static utility.hashFunctions.stringHash;

public class Account {

    private String username;
    private byte[] password;//hash password
    private ClientInterface stub;
    private String publicKey;
    private int accountId;//l'indice dove si trova nella lista degli account

    /**
     * Crea un nuovo account
     * @param userName l'username :D
     * @param plainPassword (password in chiaro che verrà criptata)
     * @param stub lo del client
     * @param publicKey la chiave pubblica del client
     * @param accountId deve essere >=0 in quanto si riferisce anche alla posizione all'interno della accountList del server
     * @throws NullPointerException() se username o password corrispondono a null;gli altri possono essere passati come null
     */
    public Account(String userName, String plainPassword, ClientInterface stub, String publicKey,int accountId)
    throws  NullPointerException,NoSuchAlgorithmException,IllegalArgumentException {
        if (userName == null || plainPassword == null) {
            throw new NullPointerException("username o plainpassword == null");
        } else {
            if(accountId<0)
            {
                throw new IllegalArgumentException("accountId < 0");
            }
            this.username = userName;
            this.password = stringHash(plainPassword);
            this.stub = stub;
            this.publicKey = publicKey;
            this.accountId=accountId;
        }
    }


    /**
     * Crea un nuovo account differenza con il precente sulla password (già hashata)
     * @param userName l'username :D
     * @param password (password hashata)
     * @param stub lo del client
     * @param publicKey la chiave pubblica del client
     * @param accountId deve essere >=0 in quanto si riferisce anche alla posizione all'interno della accountList del server
     * @throws NullPointerException() se username o password corrispondono a null;gli altri possono essere passati come null
     */
    public Account(String userName, byte[] password, ClientInterface stub, String publicKey,int accountId)
            throws  NullPointerException,IllegalArgumentException {
        if (userName == null) {
            throw new NullPointerException("username o plainpassword == null");
        } else {
            if(accountId<0)
            {
                throw new IllegalArgumentException("accountId < 0");
            }
            this.username = userName;
            this.password = password;
            this.stub = stub;
            this.publicKey = publicKey;
            this.accountId=accountId;
        }


    }



    /**
     * Compara la password in chiaro passata (successivamente hashata con l'hash di quella salvata
     * @param plainPassword password in chiaro, se null viene settata come stringa vuota
     * @return true se corrispondono, false altrimenti
     */
    public  boolean cmpPassword(String plainPassword) throws NoSuchAlgorithmException
    {
        if(plainPassword==null || plainPassword.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        return compareHashandString(this.password, plainPassword);
    }

    /**getter**/
    public int getAccountId() {
        return accountId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public byte[] getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public ClientInterface getStub() {
        return stub;
    }


    /**setter*/
    public void setAccountId(int accountId) {
        if(accountId<0)
        {
            throw new IllegalArgumentException("accountId < 0");//non era strettamente necessario ma vabbè
        }
        this.accountId = accountId;
    }

    public void setEncryptedPassword(byte[] encryptedPassword) {
        if(password==null)throw new IllegalArgumentException("password==null");
        this.password = encryptedPassword;
    }

    public void encryptAndSetPassword(String plainPassword)throws NoSuchAlgorithmException{
        if(plainPassword==null){throw new IllegalArgumentException("password==null");}
        setEncryptedPassword(stringHash(plainPassword));
    }

    public void setStub(ClientInterface stub) {
        this.stub = stub;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setUsername(String username) {
        if(username==null)throw new IllegalArgumentException("username==null");
        this.username = username;
    }

    public Account copy()  {
        return new Account(username, password, stub, publicKey, accountId);
    }
}
