package client;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;
import utility.Message;
import utility.ResponseCode;

import java.rmi.RemoteException;

public class Client implements ClientInterface {

    private String username;
    private String light_password;
    private ClientInterface stub;
    private ServerInterface skeleton;//broker stub
    private static long cookie;
    private String bp_key; //broker public key
    private String my_private_key;

    public ResponseCode notify(Message m) throws RemoteException;
    boolean isAlive() throws RemoteException;

}
