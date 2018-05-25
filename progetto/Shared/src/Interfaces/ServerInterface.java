package Interfaces;

import utility.ResponseCode;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote,Serializable
{

    ResponseCode register(String usn, String pwd) throws RemoteException;

    ResponseCode connect(String usn,String pwd) throws RemoteException;

    void disconnect() throws RemoteException;

    void subscribe() throws RemoteException;

    void unsubscribe() throws RemoteException;

    void publish() throws RemoteException;

    void ping() throws RemoteException;

    void getTopicList() throws RemoteException;
}
