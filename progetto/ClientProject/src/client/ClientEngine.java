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

    private LinkedBlockingQueue<Event> clientEngineToGUI;//la coda riempita da clientEngine e consumata dalla GUI o da TerminalInterface
    private LinkedBlockingQueue<Event> guiToClientEngine;//la coda riempita da GUI o TerminalInterface e consumata da ClientEngine
    private AnonymousClient client;
    private ServerInfoRecover infoServer;//La classe per recuperare la porta del server e stabilire la connessione

    public ClientEngine(LinkedBlockingQueue<Event>clientEngineToGUI, LinkedBlockingQueue<Event> guiToClientEngine) throws IOException {
        this.clientEngineToGUI=requireNonNull(clientEngineToGUI);
        this.guiToClientEngine=requireNonNull(guiToClientEngine);
        infoServer = new ServerInfoRecover();
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
                        case UNSUBSCRIBE:
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
                            //todo
                            break;
                        case LOGIN:
                            try {
                                client = new Client(((AccountLoginWindow) current).getUsername(), ((AccountLoginWindow) current).getPassword(), null);
                                String[] a = infoServer.getServerInfo(
                                        InetAddress.getByName(((AnonymousLoginWindow) current).getServerAddress()), 6000);    //todo agiongere la ricerca della porta - quella base è 6000 ma se ci sono più server nella stessa macchina potrebbe essere diversa
                                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                                if (((Client) client).retrieveAccount()) {
                                    current = new ForumWindow();//todo settare la roba da passare
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
                                        InetAddress.getByName(((NewAccountWindow) current).getServerAddress()), 6000
                                );
                                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                                if (((Client) client).register()) {
                                    current = new ForumWindow();//todo settare la roba da passare
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
                                    current = new ForumWindow();//todo settare la roba da passare
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
                                    current = new AccountLoginWindow();//todo settare la roba da passare
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
                if (this.client != null && this.client.anonymousClientToClientEngine.peek() != null) {
                    clientEngineToGUI.add(this.client.anonymousClientToClientEngine.poll());
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
