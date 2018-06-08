package interfaces;

import utility.ResponseCode;
import utility.Topic;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote,Serializable
{

    ResponseCode register(String userName, String plainPassword, ClientInterface stub, String publicKey) throws RemoteException;

    ResponseCode anonymousRegister(ClientInterface stub, String publicKey)throws RemoteException;

    ResponseCode retrieveAccount(String username, String plainPassword, ClientInterface clientStub, String cookie)throws RemoteException;

    ResponseCode connect(ClientInterface clientStub, String clientPublicKey) throws RemoteException;

    ResponseCode disconnect(String cookie) throws RemoteException;

    void subscribe(String cookie, Topic topic) throws RemoteException;

    void unsubscribe(String cookie,Topic topic) throws RemoteException ;

    void publish(String cookie) throws RemoteException;

    void ping() throws RemoteException;

    List<Topic> getTopicList() throws RemoteException;
}
