package utility;


import Interfaces.ClientInterface;

import static utility.hashFunctions.compareHashandString;
import static utility.hashFunctions.stringHash;

public class Account {
    private String username;
    private int password;//hash password
    private ClientInterface stub;
    private String publicKey;


    /**
     * Crea un nuovo account
     * @param userName l'username :D
     * @param plainPassword (password in chiaro che verrà criptata)
     * @param stub lo del client
     * @param  publicKey la chiave pubblica del client
     * @throws NullPointerException() se username o password corrispondono a null;gli altri possono essere passati come null
     */
    public Account(String userName, String plainPassword, ClientInterface stub, String publicKey)
    throws  NullPointerException {
        if (userName == null || plainPassword == null) {
            throw new NullPointerException("username o plainpassword == null");
        } else {
            this.username = userName;
            this.password = stringHash(plainPassword);
            this.stub = stub;
            this.publicKey = publicKey;
        }
    }


    /**
     * Compara la password in chiaro passata (successivamente hashata con l'hash di quella salvata
     * @param plainPassword password in chiaro, se null viene settata come stringa vuota
     * @return true se corrispondono, false altrimenti
     */
    public boolean cmpPassword(String plainPassword)
    {
        if(plainPassword==null)
        {
            plainPassword="";
        }
        return compareHashandString(this.password,plainPassword);

    }
}
