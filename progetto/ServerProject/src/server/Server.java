package server;

import Interfaces.ServerInterface;
import utility.ResponseCode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

public class Server implements ServerInterface{
    //STRUTTURE DATI
    /*
    *   lista dei topic
    *   hashMap topic,client
    *   lista dei client che si sono registrati
    *
     */
    private AccountCollectionInterface accountList;//monitor della lista contente tutti gli account salvati
    private Properties serverSettings=new Properties();//setting del server


    /**Costruttore
     *carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     *  se il file di config non viene trovato
     */
    public Server()
    {
        try {
            FileInputStream in = new FileInputStream("config.serverSettings");
            serverSettings.load(in);
            in.close();
            this.accountList=new AccountListMonitor(Integer.parseInt(serverSettings.getProperty("maxaccountnumber")));
                    //TODO aggiungere i costruttori con i loro setting

        }catch (IOException exc){
            this.accountList=new AccountListMonitor();//usa il default
        }
    }


    /* ********************************************************************************************************** **/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */




    /* ************************************************************************************************************/
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

    /* ************************************************************************************************************/
    //METODI PRIVATI






}
