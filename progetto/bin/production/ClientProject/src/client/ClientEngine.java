package client;

import Events.*;
import utility.Message;
import utility.infoProvider.ServerInfoRecover;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Objects.requireNonNull;


public class ClientEngine implements Callable<Integer> {

    private LinkedBlockingQueue<Event> clientEngineToGUI;//la coda riempita da clientEngine e consumata dalla GUI o da TerminalInterface
    private LinkedBlockingQueue<Event> guiToClientEngine;//la coda riempita da GUI o TerminalInterface e consumata da ClientEngine
    private AnonymousClient client;
    private ServerInfoRecover infoServer;//La classe per recuperare la porta del server e stabilire la connessione
    private ConcurrentHashMap<String,LinkedBlockingQueue<Message>> topicMessageListMap;//associazione tra topic e messaggi arrivati(salvati in una lista

    public ClientEngine(LinkedBlockingQueue<Event>clientEngineToGUI, LinkedBlockingQueue<Event> guiToClientEngine) throws IOException {
        this.clientEngineToGUI=requireNonNull(clientEngineToGUI);
        this.guiToClientEngine=requireNonNull(guiToClientEngine);
        infoServer = new ServerInfoRecover();
        this.topicMessageListMap= new ConcurrentHashMap<>();
    }

    /** Questo Thread è il "motore" dell'applicazione lato client.
     * Si occupa della gestione degli eventi passati dall'utente tramite la coda guiToClientEngine.
     * Questo metodo è un loop di 4 steps:
     * 1)(Bloccante) ClientEngine aspetta che venga inserito nella coda un evento da gestire.
     *
     * 2)Acquisisce l'evento e ne riconosce la tipologia eseguendo le opeazioni necessarie per soddisfare la richiesta dell'utente.
     *
     * 3)Viene creato l'evento risposta da inserire nella coda clientEngine(spesso il solito oggetto acquisito in precedenza).
     *  Se avviene un'errore viene settato  con il field errore=true
     *
     * 4)Viene controllato se il server ,tramite la coda concorrente anonymousClientToClientEngine con il ref. in anonymousClient, ci ha notificato
     *  della creazione di un nuovo topic da parte di qualche utente o se ci ha notificato tramite notify() di un nuovo messaggio pubblicato
     *  Se presente un nuovo evento questo  viene inoltrato sulla coda clientEngineToGui per essere stampato su schermo.
     *
     * @return 0 se l'uscita dal ciclo è senza errori, 1 altrimenti.
     */

    @Override
    public Integer call() {
        try {
            Event current;
            Event temp;
            int exitCode = 0;
            loop:
            do {
                try {
                    current = guiToClientEngine.take();//FIRST STEP
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return 1;//ritorna con errore!
                }
                //2 STEP start
                if (current instanceof ClientEvent) {
                    switch (((ClientEvent) current).getType()) {
                        case SHUTDOWN:
                            if (((ShutDown) current).isErrExit()) {
                                exitCode = 1;//errore
                            }
                            break loop;
                        case DISCONNECT:
                            if (client != null && !client.disconnect()) {
                                ((Disconnect) current).setErrExit(true);
                            }
                            break;
                        case GETALLTOPICS:
                            try {
                                ((GetAllTopics) current).setTopicsList(client.getTopics());
                            } catch (Exception exc) {
                                ((GetAllTopics) current).setErr(true);
                            }
                            break;
                        case GETTOPICS:
                            try {
                                List<String> list = new LinkedList<>();
                                Iterator<String> it = client.getTopicsSubscribed();
                                while (it.hasNext()) {
                                    list.add(it.next());
                                }
                                ((GetTopics) current).setTopicsList(list.toArray(new String[0]));

                            } catch (Exception exc) {
                                ((GetTopics) current).setErr(true);
                            }
                            break;
                        case SUBSCRIBE:
                            try {
                                if (client == null || !client.subscribe(((Subscribe) current).getTopicName())) {
                                    ((Subscribe) current).setErr(true);
                                }
                            } catch (Exception exc) {
                                ((Subscribe) current).setErr(true);
                            }
                            break;
                        case UNSUBSCRIBE://todo eliminare il topic dalla topicMessageListMap
                            try {
                                if (client == null || !client.unsubscribe(((UnSubscribe) current).getTopicName())) {
                                    ((UnSubscribe) current).setErr(true);
                                }
                            } catch (Exception exc) {
                                ((UnSubscribe) current).setErr(true);
                            }
                            break;
                        case PUBLISH:
                            try {
                                if (client == null || !client.publish(((Publish) current).getTopicName(), ((Publish) current).getTitle(), ((Publish) current).getText())) {
                                    ((Publish) current).setErr(true);
                                }

                            } catch (Exception exc) {
                                ((Publish) current).setErr(true);
                            }
                            break;

                    }
                } else if (current instanceof Window) {
                    switch (((Window) current).getWindowType()) {
                        case FORUM:
                            current = new ForumWindow();
                            ((ForumWindow) current).setReceivedMessageList(this.topicMessageListMap);
                            break;
                        case LOGIN:
                            try {

                                client = new Client(((AccountLoginWindow) current).getUsername(), ((AccountLoginWindow) current).getPassword(), null);
                                String[] a = infoServer.getServerInfo(
                                        InetAddress.getByName(((AnonymousLoginWindow) current).getServerAddress()), 6000);    //todo agiongere la ricerca della porta - quella base è 6000 ma se ci sono più server nella stessa macchina potrebbe essere diversa
                                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                                if ( client.retrieveAccount()) {
                                    temp = new ForumWindow();
                                    ((ForumWindow) temp).setReceivedMessageList(this.topicMessageListMap);
                                    current=temp;
                                } else {
                                    ((AnonymousLoginWindow) current).setErr(true);
                                }

                            } catch (Exception exc) {
                                ((AnonymousLoginWindow) current).setErr(true);
                                exc.printStackTrace();
                            }
                            break;
                        case NEWACCOUNT:
                            try {
                                client = new Client(((NewAccountWindow) current).getUsername(), ((NewAccountWindow) current).getPassword(), ((NewAccountWindow) current).getEmail());
                                String[] a = infoServer.getServerInfo(
                                        InetAddress.getByName(((NewAccountWindow) current).getServerAddress()), 41393
                                );
                                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                                if (client.register()) {
                                    temp = new ForumWindow();
                                    ((ForumWindow) temp).setReceivedMessageList(this.topicMessageListMap);
                                    current=temp;
                                } else {
                                    ((NewAccountWindow) current).setErr(true);
                                }
                            } catch (Exception exc) {
                                ((NewAccountWindow) current).setErr(true);
                                exc.printStackTrace();
                            }

                            break;
                        case ANONYMOUSLOGIN:
                            try {
                                client = new AnonymousClient();
                                String[] a = infoServer.getServerInfo(
                                        InetAddress.getByName(((AnonymousLoginWindow) current).getServerAddress()), 6000
                                );
                                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                                if (client.register()) {
                                    temp = new ForumWindow();
                                    ((ForumWindow) temp).setReceivedMessageList(this.topicMessageListMap);
                                    current=temp;
                                } else {
                                    ((AnonymousLoginWindow) current).setErr(true);
                                }
                            } catch (Exception exc) {
                                ((AnonymousLoginWindow) current).setErr(true);
                                exc.printStackTrace();
                            }
                            break;
                        case FORGOTPASSWORD:
                            try {
                                client = new AnonymousClient();
                                String[] a = infoServer.getServerInfo(
                                        InetAddress.getByName(((ForgotPasswordWindow) current).getServerAddress()), 6000
                                );
                                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                                if (client.recoverPassword(((ForgotPasswordWindow) current).getEmail(), ((ForgotPasswordWindow) current).getNewPassword(), ((ForgotPasswordWindow) current).getRepeatPassword())) {
                                    temp = new AccountLoginWindow();//todo settare la roba da passare

                                    current=temp;
                                } else {
                                    ((ForgotPasswordWindow) current).setErr(true);
                                }
                            } catch (Exception exc) {
                                ((ForgotPasswordWindow) current).setErr(true);
                                exc.printStackTrace();

                            }
                            break;

                    }
                }
                //2 STEP END

                clientEngineToGUI.add(current);//3 STEP

                //4 STEP start
                if (this.client != null && (current=this.client.anonymousClientToClientEngine.poll()) != null) {
                    switch (((ClientEvent) current).getType()) {
                        case NEWTOPICNOTIFICATION: break;
                        case NEWMESSAGE:
                            Message message=((NewMessage)current).getMessage();
                            LinkedBlockingQueue<Message> returnValMessageList;
                            LinkedBlockingQueue<Message> justCreatedMessList=new LinkedBlockingQueue<>();

                            if((returnValMessageList=topicMessageListMap.putIfAbsent(message.getTopic(),justCreatedMessList))==null){//topic non ancora registrato
                                justCreatedMessList.add(message);
                            }else{//topic già registrato in precedenza
                                returnValMessageList.add(message);
                            }
                            break;
                    }
                    clientEngineToGUI.add(current);
                }
                //4 STEP end

            } while (true);
            return exitCode;
        }catch (Exception exc){
            exc.printStackTrace();
        }
        return 1;
    }
}
