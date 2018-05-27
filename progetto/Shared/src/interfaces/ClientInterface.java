package interfaces;

import utility.Message;
import utility.ResponseCode;

import java.io.Serializable;
import java.rmi.Remote;

public interface ClientInterface extends Remote,Serializable
{
    ResponseCode notify(Message m) /*throws RemoteException*/;

    boolean isAlive() /* throws RemoteException*/;

}

