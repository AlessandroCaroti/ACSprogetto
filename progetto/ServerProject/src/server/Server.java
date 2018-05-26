package server;

import Interfaces.ServerInterface;
import utility.ResponseCode;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server implements ServerInterface{
    //STRUTTURE DATI
    /*
    *   lista dei topic
    *   hashMap topic,client
    *   lista dei client che si sono registrati
    *
     */
    private ArrayList accountList=new ArrayList();






    /*************************************************************************************************************/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */




    /*************************************************************************************************************/
    //METODI REMOTI

    @Override
    public ResponseCode register(String usn, String pwd) throws RemoteException {
        ResponseCode rc;
        rc=new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.SERVER,
                "OK Il client"+usn+ "è stato registrato correttamente");
        return rc;
    }

    @Override
    public ResponseCode connect(String usn,String pwd) throws RemoteException {
        ResponseCode rc;
        rc=new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.SERVER,
                "OK Il client"+usn+ "si è connesso correttamente");
        return rc;
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
