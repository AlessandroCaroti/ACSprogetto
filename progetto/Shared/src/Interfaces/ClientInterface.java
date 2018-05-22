package Interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote,Serializable
{
    void notify(int resp) throws RemoteException;

    boolean isAlive()  throws RemoteException;

}

