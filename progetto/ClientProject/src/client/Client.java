package client;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;
import utility.Message;
import utility.ResponseCode;

import java.rmi.RemoteException;

public class Client implements ClientInterface {
    //STRUTTURE DATI
    private String username;
    private String light_password;
    private ClientInterface stub;
    private ServerInterface skeleton;//broker stub
    private static long cookie;
    private String bp_key; //broker public key
    private String my_private_key;

    /*************************************************************************************************************/
    //API





    /*************************************************************************************************************/
    //METODI REMOTI

    @Override
    public ResponseCode notify(Message m) throws RemoteException {
        return null;
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return false;
    }

    /*************************************************************************************************************/
    //METODI PRIVATI
}
