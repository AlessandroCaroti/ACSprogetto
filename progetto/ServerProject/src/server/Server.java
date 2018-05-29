package server;

import interfaces.ServerInterface;
import interfaces.ClientInterface;
import utility.ResponseCode;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements ServerInterface,Callable<Integer> {
    //STRUTTURE DATI
    /*
    *   lista dei topic
    *   lista dei client anonimi
    *
     */
    private AccountCollectionInterface accountList;         //monitor della lista contente tutti gli account salvati
    private Properties serverSettings=new Properties();     //setting del server
    private ConcurrentHashMap<byte[],Integer> topicClientAssociation;   //aca gennaro hashMap contenete le associazioni topic->clientOnlie


    /**Costruttore
     *carica automaticamente i setting da file.
     * Se il file non viene trovato vengono usati i costruttori di default
     *  se il file di config non viene trovato
     */
    Server()
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

    /**
     * Avvia il server
     * @return 0 quando l'utente vuole spegnere l'host,1 in caso di errore dell'interfaccia grafica
     */
    public Integer call(){




        return 0;
    }

    /* ********************************************************************************************************** **/
    //API

    /*TODO
        aggiungere i metodi elencari nel file che specifica le API del server
     */




    /* ************************************************************************************************************/
    //METODI REMOTI

    @Override
    public ResponseCode register(ClientInterface skeleton,String usn, String pwd) throws RemoteException {
        ResponseCode rc;
        rc=new ResponseCode(ResponseCode.Codici.R100, ResponseCode.TipoClasse.SERVER,
                "OK Il client"+usn+ "è stato registrato correttamente");

        return rc;
    }

    @Override
    public ResponseCode connect(ClientInterface skeleton,String usn,String pwd) throws RemoteException {
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
