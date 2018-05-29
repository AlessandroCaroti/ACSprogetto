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
     * Crea un nuovo account
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
    public boolean cmpPassword(String plainPassword) throws NoSuchAlgorithmException
    {
        if(plainPassword==null)
        {
            plainPassword="";
        }
        return compareHashandString(this.password, plainPassword);
    }

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

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
