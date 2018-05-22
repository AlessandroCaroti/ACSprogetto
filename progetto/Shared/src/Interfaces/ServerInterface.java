package Interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote,Serializable
{

    void register() throws RemoteException;

    void connect() throws RemoteException;

    void disconnect() throws RemoteException;

    void subscribe() throws RemoteException;

    void unsubscribe() throws RemoteException;

    void publish() throws RemoteException;

    void ping() throws RemoteException;

    void getTopicList() throws RemoteException;
}
