package utility;


import Interfaces.ClientInterface;
import Interfaces.ServerInterface;

public class Account {
    private String username;
    private String password;    //hash password
    private ClientInterface stub;
    private String publicKey;

    public Account(String usrname, String psswd, ClientInterface stub, String publicKey)
    {

    }

    public boolean cmpPassword(String password)
    {
        return false;
    }
}
