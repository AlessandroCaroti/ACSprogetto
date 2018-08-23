package client;

import Events.*;
import utility.ServerInfoRecover;

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
        boolean uscita=false;
        do{
            try {
                current = guiToClientEngine.take();
            }catch(InterruptedException e){
                e.printStackTrace();
                return 1;//ritorna con errore!
            }
            if(current instanceof ClientEvent){
                switch(((ClientEvent) current).getType()){
                    case SHUTDOWN:
                        uscita=true;
                        break;
                    case DISCONNECT:
                        current=new Disconnect();
                        if(!client.disconnect()){
                            ((Disconnect) current).setErrExit(true);
                        }
                        clientEngineToGUI.add(current);
                        current=null;
                        break;
                    case GETALLTOPICS:
                        try {
                            ((GetAllTopics) current).setTopicsList(client.getTopics());
                        }catch (Exception exc){
                            ((GetAllTopics) current).setErr(true);
                        }
                        clientEngineToGUI.add(current);
                        current=null;
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
                        clientEngineToGUI.add(current);
                        current=null;
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
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress())
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(((Client)client).retrieveAccount()){
                                System.out.println("Account recuperato");
                                clientEngineToGUI.add(new ForumWindow());//todo settare la roba da passare
                            }else{
                                System.out.println("NON recuperato");
                                ((AnonymousLoginWindow)current).setErr(true);
                                clientEngineToGUI.add(current);
                                current=null;
                            }
                        }catch(Exception exc){
                            //todo avviene quando: infoprovider non inizializzato oppure unicast object fallito
                            exc.printStackTrace();
                        }
                        break;
                    case NEWACCOUNT:
                        try {
                            client = new Client(((NewAccountWindow)current).getUsername(), ((NewAccountWindow)current).getPassword(),((NewAccountWindow)current).getEmail());
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((NewAccountWindow)current).getServerAddress())
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(((Client) client).register()){
                                System.out.println("Account creato");
                                clientEngineToGUI.add(new ForumWindow());//todo settare la roba da passare
                            }else{
                                System.out.println("NON creato");
                                ((NewAccountWindow)current).setErr(true);
                                clientEngineToGUI.add(current);
                                current=null;

                            }
                        }catch(Exception exc){
                            //todo
                            exc.printStackTrace();
                        }

                        break;
                    case ANONYMOUSLOGIN:
                        try {
                            client = new AnonymousClient();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress())
                                    );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if (client.register()) {
                                clientEngineToGUI.add(new ForumWindow());//todo settare la roba da passare
                            } else {
                                ((AnonymousLoginWindow)current).setErr(true);
                                clientEngineToGUI.add(current);
                                current=null;
                            }
                        }catch(Exception exc){
                            //todo
                            exc.printStackTrace();
                        }
                            break;
                    case FORGOTPASSWORD:
                        try {
                            client = new AnonymousClient();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((ForgotPasswordWindow)current).getServerAddress())
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(client.recoverPassword(((ForgotPasswordWindow)current).getEmail(),((ForgotPasswordWindow)current).getNewPassword(),((ForgotPasswordWindow)current).getRepeatPassword()))
                            {
                                clientEngineToGUI.add(new AccountLoginWindow());//todo settare la roba da passare
                            } else {
                                ((ForgotPasswordWindow)current).setErr(true);
                                clientEngineToGUI.add(current);
                                current=null;
                            }
                        }catch(Exception exc){
                            //todo
                            exc.printStackTrace();

                        }
                        break;
                        default://todo da eliminare fine debugging
                            System.err.println("uknown command");
                            break;


                }
            }



        }while(!uscita);
        return 0;
    }
}
