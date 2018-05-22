package sheredInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInt extends Remote,Serializable
{
    void notify(int resp) throws RemoteException;

    boolean isAlive()  throws RemoteException;

}
