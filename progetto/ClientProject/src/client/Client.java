package client;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;
import utility.Message;
import utility.ResponseCode;
import utility.Topic;

import java.rmi.RemoteException;

public class Client implements ClientInterface {

    //Variabili di istanza

    private String username;
    private String light_password;
    private ClientInterface stub;
    private ServerInterface skeleton;//broker stub
    private long cookie;
    private String bp_key; //broker public key
    private String my_private_key;
    private static long last_cookie;

    /*************************************************************************************************************/
    //Costruttore

    public Client(String username, String light_password, ClientInterface stub, ServerInterface skeleton,
                  String bp_key, String my_private_key)
    {
        if(username==null||light_password==null||stub==null||skeleton==null||bp_key==null||my_private_key==null)
            throw new NullPointerException();

        this.username=username;
        this.light_password=light_password;
        this.stub=stub;
        this.skeleton=skeleton;
        this.cookie=last_cookie++;
        this.bp_key=bp_key;
        this.my_private_key=my_private_key;
    }

    /*************************************************************************************************************/
    //API

    /*TODO
    aggiungere i metodi elencari nel file che specifica le API del client
     */


    /*************************************************************************************************************/
    //METODI REMOTI

    @Override
    public ResponseCode notify(Message m) throws RemoteException {
        if(m==null) {
            ResponseCode rc=new ResponseCode(ResponseCode.Codici.R400, ResponseCode.TipoClasse.CLIENT,
                    "NOT OK Il server ha ricevuto un messaggio vuoto");
            return rc;
        }
        ResponseCode rc=new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.CLIENT,
                "OK il server ha ricevuto il messaggio");
        return rc;
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }

    /*************************************************************************************************************/
    //METODI PRIVATI
}
