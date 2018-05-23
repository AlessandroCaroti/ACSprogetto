package utility;


import Interfaces.ClientInterface;
import Interfaces.ServerInterface;

public class Account {
    private String username;
    private String password;    //hash password
    private ClientInterface stub;
    private String publicKey;

    /**
     * Crea un nuovo account
     * @param userName l'username :D
     * @param plainPassword (password in chiaro che verrà criptata)
     * @param stub lo del client
     * @param  publicKey la chiave pubblica del client
     * @throws NullPointerException() se username o password corrispondono a null gli altri possono essere passati come null
     */
    public Account(String userName, String plainPassword, ClientInterface stub, String publicKey)
    throws  NullPointerException
    {
        if(userName==null||plainPassword==null)
        {
            throw new NullPointerException("username o plainpassword == null");
        }
        this.username=userName;
        this.password="ciao";
        this.stub=stub;
        this.publicKey=publicKey;
    }

    public boolean cmpPassword(String password)
    {
        
        return false;
    }
}
