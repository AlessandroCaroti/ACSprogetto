package client;

public class Client implements ClientInterface{
    private String username;
    private String mp_pwd; //my public password
    private ClientInterface stub;
    private ServerInterface skeleton; //broker stub
    private static long cookie;
    private String bp_key; //broker public key
    private String mp_key; //my private key

    public ResponseCode notify(Message m) throws RemoteException;
    boolean isAlive() throws RemoteException;
}
