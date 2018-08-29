package server;

import client.AnonymousClient;
import utility.LogFormatManager;
import utility.Message;
import utility.ResponseCode;
import java.rmi.RemoteException;
import static java.util.Objects.requireNonNull;

/** Questa classe differisce da AnonymousClient per i due metodi  notify() e newTopicNotification()*/
public class AnonymousClientExtended extends AnonymousClient {

    private Server server;


    AnonymousClientExtended(Server server)throws NullPointerException,RemoteException{
        super();
        this.server=requireNonNull(server);
        this.className="ANONYMOUS_CLIENT_EXTENDED";
        print = new LogFormatManager("ANONYMOUS_CLIENT_EXTENDED", false);
    }

    @Override
    public ResponseCode notify(Message m) {
        ResponseCode rc;
        if(m==null) {
            rc=new ResponseCode(ResponseCode.Codici.R500, ResponseCode.TipoClasse.CLIENT, "(-) WARNING Il client ha ricevuto un messaggio vuoto");
        }else {
            print.pedanticInfo("Received new message\n" + m.toString());
            rc = new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.CLIENT, "(+) OK il client ha ricevuto il messaggio");

            server.forwardMessage(m);//inoltra il messaggio sul proprio server

        }
        return rc;
    }


    @Override
    public void newTopicNotification(String topicName){

        server.addTopic(topicName);
        try {
            this.server_stub.subscribe(this.getCookie(), topicName);
        }catch (Exception exc){//se fallisce rimuovo il topic dalla lista del mio server
            exc.printStackTrace();
            server.removeTopic(topicName);
        }
    }

}
