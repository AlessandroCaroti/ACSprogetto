package interfaces;

import utility.ResponseCode;
import utility.Topic;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote,Serializable
{

    ResponseCode register(String userName, String plainPassword, ClientInterface stub, String publicKey) ;

    ResponseCode anonymousRegister(ClientInterface stub, String publicKey);

    ResponseCode retrieveAccount(String username,String plainPassword,ClientInterface clientStub,String cookie);

    ResponseCode connect(ClientInterface clientStub,String clientPublicKey) ;

    ResponseCode disconnect(String cookie) ;

    void subscribe(String cookie, Topic topic) ;

    void unsubscribe(String cookie,Topic topic) ;

    void publish(String cookie) ;

    void ping() ;

    List<Topic> getTopicList() ;
}
