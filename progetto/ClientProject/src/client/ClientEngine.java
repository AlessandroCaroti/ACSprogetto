package client;

import Events.*;
import utility.infoProvider.ServerInfoRecover;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Objects.requireNonNull;


public class ClientEngine implements Callable<Integer> {

    private LinkedBlockingQueue<Event> clientEngineToGUI;
    private LinkedBlockingQueue<Event> guiToClientEngine;
    private AnonymousClient client;
    private ServerInfoRecover infoServer;

    public ClientEngine(LinkedBlockingQueue<Event>clientEngineToGUI, LinkedBlockingQueue<Event> guiToClientEngine) throws IOException {
        this.clientEngineToGUI=requireNonNull(clientEngineToGUI);
        this.guiToClientEngine=requireNonNull(guiToClientEngine);
        infoServer = new ServerInfoRecover();
    }


    @Override
    public Integer call() {
        Event current;
        int exitCode=0;
        loop:do{
            try {
                current = guiToClientEngine.take();
            }catch(InterruptedException e){
                e.printStackTrace();
                return 1;//ritorna con errore!
            }
            if(current instanceof ClientEvent){
                switch(((ClientEvent) current).getType()){
                    case SHUTDOWN:
                        if(((ShutDown)current).isErrExit()){
                            exitCode=1;//errore
                        }
                        break loop;
                    case DISCONNECT:
                        if(client!=null&&!client.disconnect()){
                            ((Disconnect) current).setErrExit(true);
                        }
                        break;
                    case GETALLTOPICS:
                        try {
                            ((GetAllTopics) current).setTopicsList(client.getTopics());
                        }catch (Exception exc){
                            ((GetAllTopics) current).setErr(true);
                        }
                        break;
                    case GETTOPICS:
                        try {
                            List<String> list=new LinkedList<>();
                            Iterator<String> it=client.getTopicsSubscribed();
                            while(it.hasNext()){
                                list.add(it.next());
                            }
                            ((GetTopics) current).setTopicsList(list.toArray(new String[0]));

                        }catch (Exception exc){
                            ((GetTopics) current).setErr(true);
                        }
                        break;
                    case SUBSCRIBE:
                        try{
                            if(client==null||!client.subscribe(((Subscribe)current).getTopicName())){
                                ((Subscribe) current).setErr(true);
                            }
                        }catch(Exception exc){
                            ((Subscribe) current).setErr(true);
                        }
                        break;
                    case UNSUBSCRIBE:
                        try{
                            if(client==null||!client.unsubscribe(((UnSubscribe)current).getTopicName())){
                                ((UnSubscribe) current).setErr(true);
                            }
                        }catch(Exception exc){
                            ((UnSubscribe) current).setErr(true);
                        }
                        break;
                    case PUBLISH:
                        try{
                            if(client==null||!client.publish(((Publish)current).getTopicName(),((Publish)current).getTitle(),((Publish)current).getText())){
                                ((Publish) current).setErr(true);
                            }

                        }catch(Exception exc){
                            ((Publish)current).setErr(true);
                        }
                        break;

                }
            }else if(current instanceof Window){
                switch (((Window) current).getWindowType()){
                    case FORUM:
                        //todo
                        break;
                    case LOGIN:
                        try {
                            client = new Client(((AccountLoginWindow)current).getUsername(), ((AccountLoginWindow)current).getPassword(),null);
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress()),6000);    //todo agiongere la ricerca della porta - quella base è 6000 ma se ci sono più server nella stessa macchina potrebbe essere diversa
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(((Client)client).retrieveAccount()){
                                current=new ForumWindow();//todo settare la roba da passare
                            }else{
                                ((AnonymousLoginWindow)current).setErr(true);
                            }
                        }catch(Exception exc){
                            ((AnonymousLoginWindow)current).setErr(true);
                            exc.printStackTrace();
                        }
                        break;
                    case NEWACCOUNT:
                        try {
                            client = new Client(((NewAccountWindow)current).getUsername(), ((NewAccountWindow)current).getPassword(),((NewAccountWindow)current).getEmail());
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((NewAccountWindow)current).getServerAddress()), 6000
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(((Client) client).register()){
                                current=new ForumWindow();//todo settare la roba da passare
                            }else{
                                ((NewAccountWindow)current).setErr(true);
                            }
                        }catch(Exception exc){
                            ((NewAccountWindow)current).setErr(true);
                            exc.printStackTrace();
                        }

                        break;
                    case ANONYMOUSLOGIN:
                        try {
                            client = new AnonymousClient();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress()), 6000
                                    );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if (client.register()) {
                                current=new ForumWindow();//todo settare la roba da passare
                            } else {
                                ((AnonymousLoginWindow)current).setErr(true);
                            }
                        }catch(Exception exc){
                            ((AnonymousLoginWindow)current).setErr(true);
                            exc.printStackTrace();
                        }
                            break;
                    case FORGOTPASSWORD:
                        try {
                            client = new AnonymousClient();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((ForgotPasswordWindow)current).getServerAddress()), 6000
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(client.recoverPassword(((ForgotPasswordWindow)current).getEmail(),((ForgotPasswordWindow)current).getNewPassword(),((ForgotPasswordWindow)current).getRepeatPassword()))
                            {
                                current=new AccountLoginWindow();//todo settare la roba da passare
                            } else {
                                ((ForgotPasswordWindow)current).setErr(true);
                            }
                        }catch(Exception exc){
                            ((ForgotPasswordWindow)current).setErr(true);
                            exc.printStackTrace();

                        }
                        break;

                }
            }
            clientEngineToGUI.add(current);
        }while(true);
        return exitCode;
    }
}
