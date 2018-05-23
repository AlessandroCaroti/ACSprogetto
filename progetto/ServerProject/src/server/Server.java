package server;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;
import utility.Message;
import utility.ResponseCode;

import java.rmi.RemoteException;

public class Server implements ServerInterface{
    //STRUTTURE DATI
    /*
    *   lista dei topic
    *   hashMap topic,client
    *   lista dei client che si sono registrati
    *
     */





    /*************************************************************************************************************/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */




    /*************************************************************************************************************/
    //METODI REMOTI

    @Override
    public void register() throws RemoteException {

    }

    @Override
    public void connect() throws RemoteException {

    }

    @Override
    public void disconnect() throws RemoteException {

    }

    @Override
    public void subscribe() throws RemoteException {

    }

    @Override
    public void unsubscribe() throws RemoteException {

    }

    @Override
    public void publish() throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void getTopicList() throws RemoteException {

    }

    /*************************************************************************************************************/
    //METODI PRIVATI
}
