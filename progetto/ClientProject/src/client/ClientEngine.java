package client;

import Events.*;
import utility.ServerInfoRecover;

import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import static java.util.Objects.requireNonNull;


public class ClientEngine implements Callable<Integer> {

    private ConcurrentLinkedQueue<Event> clientEngineToGUI;
    private ConcurrentLinkedQueue<Event> guiToClientEngine;
    private AnonymousClient client;

    public ClientEngine(ConcurrentLinkedQueue<Event>clientEngineToGUI, ConcurrentLinkedQueue<Event> guiToClienEngine){
        this.clientEngineToGUI=requireNonNull(clientEngineToGUI);
        this.guiToClientEngine=requireNonNull(guiToClienEngine);
    }


    @Override
    public Integer call() {
        Event current;
        boolean uscita=false;
        do{

            current=guiToClientEngine.poll();
            if(current instanceof ClientEvent){
                switch(((ClientEvent) current).getType()){
                    case SWITCHOFF:
                        uscita=true;
                        break;

                }
            }
            if(current instanceof Window){
                switch (((Window) current).getWindowType()){
                    case FORUM:

                        break;
                    case LOGIN:
                        try {
                            client = new Client(((AccountLoginWindow)current).getUsername(), ((AccountLoginWindow)current).getPassword(),"tobeimpl","tobeimpl",((AccountLoginWindow)current).getEmail());
                            ServerInfoRecover infoServer = new ServerInfoRecover();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress())
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(client.retrieveAccount()){
                                System.out.println("Account recuperato");
                                clientEngineToGUI.add(new ForumWindow());//todo settare la roba da passare
                            }else{
                                System.out.println("NON recuperato");
                                /*todo come gestisco l'errore nella schermata?*/
                            }
                        }catch(Exception exc){
                            //todo
                        }
                        break;
                    case NEWACCOUNT:
                        try {
                            client = new Client(((AccountLoginWindow)current).getUsername(), ((AccountLoginWindow)current).getPassword(),"tobeimpl","tobeimpl",((AccountLoginWindow)current).getEmail());
                            ServerInfoRecover infoServer = new ServerInfoRecover();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress())
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(client.register()){
                                System.out.println("Account creato");
                                clientEngineToGUI.add(new ForumWindow());//todo settare la roba da passare
                            }else{
                                System.out.println("NON creato");
                                /*todo come gestisco l'errore nella schermata?*/
                            }
                        }catch(Exception exc){
                            //todo
                        }

                        break;
                    case ANONYMOUSLOGIN:
                        try {
                            client = new AnonymousClient("tobeimpl", "tobeimpl");
                            ServerInfoRecover infoServer = new ServerInfoRecover();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress())
                                    );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if (client.register()) {
                                clientEngineToGUI.add(new ForumWindow());//todo settare la roba da passare
                                System.out.println("REGISTRATO");
                            } else {
                                System.out.println("NON REGISTRATO");
                                /*todo come gestisco l'errore nella schermata?*/
                            }
                        }catch(Exception exc){
                            //todo
                        }
                            break;
                    case FORGOTPASSWORD:

                        break;
                }
            }



        }while(!uscita);
        return 0;
    }
}
