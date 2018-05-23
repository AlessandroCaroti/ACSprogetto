package utility;


import Interfaces.ClientInterface;
import Interfaces.ServerInterface;

public class Account {
    private String username;
    private int password;//hash password
    private ClientInterface stub;
    private String publicKey;
    private static hashFunctions hashClass=new hashFunctions();


    /* TODO */
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
            this.password = hashClass.stringHash(plainPassword);
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
        return this.password==hashClass.stringHash(plainPassword);

    }
}
