package sheredInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInt extends Remote,Serializable
{
    void register() throws RemoteException;

    void connect() throws RemoteException;

    void disconnect() throws RemoteException;

    void subscribe() throws RemoteException;

    void unsubscribe() throws RemoteException;

    void pubblish() throws RemoteException;

    void ping() throws RemoteException;

    void getTopicList() throws RemoteException;

}
