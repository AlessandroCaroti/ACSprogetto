package server;

import client.AnonymousClient;
import utility.Message;
import utility.ResponseCode;
import java.rmi.RemoteException;
import static java.util.Objects.requireNonNull;

public class AnonymousClientExtended extends AnonymousClient {

    private Server server;


    public AnonymousClientExtended(String my_public_key, String my_private_key,Server server)throws NullPointerException,RemoteException{
        super(my_public_key,my_private_key);
        this.server=requireNonNull(server);
        this.className="ANONYMOUS_CLIENT_EXTENDED";
    }

    @Override
    public ResponseCode notify(Message m) {
        ResponseCode rc;
        if(m==null) {
            rc=new ResponseCode(ResponseCode.Codici.R500, ResponseCode.TipoClasse.CLIENT, "(-) WARNING Il client ha ricevuto un messaggio vuoto");
        }else {
            pedanticInfo("Received new message\n" + m.toString());
            rc = new ResponseCode(ResponseCode.Codici.R200, ResponseCode.TipoClasse.CLIENT, "(+) OK il client ha ricevuto il messaggio");

            server.forwardMessage(m);//inoltra il messaggio sul proprio server

        }
        return rc;
    }

    @Override
    public ResponseCode newTopicNotification(String topicName){

        ResponseCode resp=super.newTopicNotification(topicName);
        if(resp.IsOK()){
            server.addTopic(topicName);
        }
        return resp;
    }


    void infoStamp(String msg){
        System.out.println("["+className+"-INFO]: " + msg);
    }

    void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }
}
