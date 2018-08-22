package client;

import Events.*;
import utility.ServerInfoRecover;

import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Objects.requireNonNull;


public class ClientEngine implements Callable<Integer> {

    private LinkedBlockingQueue<Event> clientEngineToGUI;
    private LinkedBlockingQueue<Event> guiToClientEngine;
    private AnonymousClient client;

    public ClientEngine(LinkedBlockingQueue<Event>clientEngineToGUI, LinkedBlockingQueue<Event> guiToClientEngine){
        this.clientEngineToGUI=requireNonNull(clientEngineToGUI);
        this.guiToClientEngine=requireNonNull(guiToClientEngine);
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
                        if(client.disconnect()){
                            System.out.println("DISCONNESSO");
                        }else{System.out.println("NON DISCONNESSO");}
                        break;

                }
            }
            if(current instanceof Window){
                switch (((Window) current).getWindowType()){
                    case FORUM:
                        //todo
                        break;
                    case LOGIN:
                        System.err.println("retrieving account");
                        try {
                            client = new Client(((AccountLoginWindow)current).getUsername(), ((AccountLoginWindow)current).getPassword(),null);//todo check se siamo sicuri non sia necessaria la mail
                            ServerInfoRecover infoServer = new ServerInfoRecover();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((AnonymousLoginWindow)current).getServerAddress())
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            System.err.println("retrieving account");
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
                            ServerInfoRecover infoServer = new ServerInfoRecover();
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
                        }

                        break;
                    case ANONYMOUSLOGIN:
                        try {
                            client = new AnonymousClient();
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
                                ((AnonymousLoginWindow)current).setErr(true);
                                clientEngineToGUI.add(current);
                                current=null;
                            }
                        }catch(Exception exc){
                            //todo
                        }
                            break;
                    case FORGOTPASSWORD:
                        try {
                            client = new AnonymousClient();
                            ServerInfoRecover infoServer = new ServerInfoRecover();
                            String[] a = infoServer.getServerInfo(
                                    InetAddress.getByName(((ForgotPasswordWindow)current).getServerAddress())
                            );
                            client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            if(client.recoverPassword(((ForgotPasswordWindow)current).getEmail(),((ForgotPasswordWindow)current).getNewPassword(),((ForgotPasswordWindow)current).getRepeatPassword()))
                            {
                                clientEngineToGUI.add(new AccountLoginWindow());//todo settare la roba da passare
                                System.out.println("RECUPERATA");
                            } else {
                                System.out.println("NON RECUPERATA");
                                ((ForgotPasswordWindow)current).setErr(true);
                                clientEngineToGUI.add(current);
                                current=null;
                            }
                        }catch(Exception exc){
                            //todo
                        }
                        break;
                }
            }



        }while(!uscita);
        return 0;
    }
}
