package client;

import Events.*;
import utility.infoProvider.ServerInfoRecover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

public class TerminalInterface implements Callable<Integer> {
    private LinkedBlockingQueue<Event> clientEngineToGui;//La coda per recuperare gli oggetti evento dal thread clientEngine
    private LinkedBlockingQueue<Event> guiToClientEngine;//La coda per inviare gli oggetti evento al clientEngine
    private BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));

     TerminalInterface(LinkedBlockingQueue<Event> clientEngineToGUI,LinkedBlockingQueue<Event> guiToClientEngine) {
        this.clientEngineToGui=clientEngineToGUI;
        this.guiToClientEngine=guiToClientEngine;
    }

    /**Il thread in questa funzione esegue un loop su 3 steps:
     * 1)riconosce il comando tramite parseCommand().
     * Questo metodo è bloccante fino a quando non riconosce un comando valido.
     * Restituisce uno degli oggetti evento (corrispondente al comando digitato) della pkg Events.
     *
     * 2)Inserisce l'evento ricevuto da parse command nella coda consumata da clientEngine.
     *
     * 3)Recupera la risposta dell'engine consumando la coda clientEngineToGui.
     * Per ogni evento restituito dall'engine viene fatto un check se è avvenuto un'errore.
     *
     * @return 0 se l'utente ha interrotto il loop ,1 altrimenti
     */

    public Integer call(){
        Event current;
        System.out.print(ASCIIART);
        while(true){
            current=this.parseCommand();
            this.guiToClientEngine.offer(current);

            if(current instanceof ShutDown) {break;}

            //recupero la risposta dell'engine
            try {
                current = this.clientEngineToGui.take();
                if(current instanceof Window){
                    switch(((Window) current).getWindowType()){
                        case FORUM:
                            printForum();

                            break;
                        case LOGIN:
                            if(((AnonymousLoginWindow)current).isErr()){
                                System.out.println("ERRORE:IL LOGIN NON E' ANDATO A BUON FINE");
                            }
                            break;
                        case NEWACCOUNT:
                            if(((NewAccountWindow)current).isErr()){
                                System.out.println("ERRORE:LA CREAZIONE NON E' ANDATA A BUON FINE");
                            }
                            break;
                        case ANONYMOUSLOGIN:
                            if(((AnonymousLoginWindow)current).isErr()){
                                System.out.println("ERRORE:IL LOGIN ANONIMO NON E' ANDATO A BUON FINE");
                            }
                            break;
                        case FORGOTPASSWORD:
                            if(((ForgotPasswordWindow)current).isErr()){
                                System.out.println("ERRORE:NON RECUPERATA");
                            }
                            break;
                        default:
                            System.err.println("uknown command");
                            break;

                    }



                }else if(current instanceof ClientEvent){
                    switch(((ClientEvent) current).getType()){
                        case DISCONNECT:
                            if(((Disconnect)current).isErrExit()){
                                System.out.println("ERRORE:DISCONNESSO CON ERRORE");
                            }else{
                                System.out.println("Disconnesso...");
                            }
                            break;
                        case GETALLTOPICS:
                            if(((GetAllTopics)current).isErr()){
                                System.out.println("ERRORE:IMPOSSIBILE RECUPERARE TUTTI I TOPICS");
                            }else{
                                printTopics(((GetAllTopics) current).getTopicsList());
                            }
                            break;
                        case GETTOPICS:
                            if(((GetTopics)current).isErr()){
                                System.out.println("ERRORE:IMPOSSIBILE RECUPERARE I TOPICS");
                            }else{
                                printTopics(((GetTopics) current).getTopicsList());
                            }
                            break;
                        case SUBSCRIBE:
                            if(((Subscribe)current).isErr()){
                                System.out.println("ERRORE:IMPOSSIBILE ISCRIVERSI AL TOPIC");
                            }else{
                                System.out.println("Iscrizione correttamente effettuata");
                            }
                            break;
                        case UNSUBSCRIBE:
                            if(((UnSubscribe)current).isErr()){
                                System.out.println("ERRORE:IMPOSSIBILE DISISCRIVERSI AL TOPIC");
                            }else{
                                System.out.println("Disiscrizione correttamente effettuata");
                            }
                            break;
                        case PUBLISH:
                            if(((Publish)current).isErr()){
                                System.out.println("ERRORE:IMPOSSIBILE PUBBLICARE IL MESSAGGIO");
                            }else{
                                System.out.println("Pubblicazione correttamente effettuata");
                            }
                            break;
                        case NEWMESSAGE:
                            //todo
                            break;
                        case NEWTOPICNOTIFICATION:
                            System.out.println("Nuovo topic presente sul server:"+((NewTopicNotification)current).getTopicName());
                            break;

                    }
                }


            }catch(InterruptedException exc){
                return 1;
            }
        }
        return 0;
    }

    /** Riconosce il comando inserito e crea l'evento corrispondente settando i suoi fields
     * @return l'evento corrispondente al comando digitato.
     */


    private Event parseCommand(){
        Event event=null;
        String string;
        boolean uscita=false;
        do {
            try {

                System.out.print(PROMPT);
                string = bufferedReader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(string, "\n\t ");
                if (tokenizer.hasMoreTokens()) {


                    switch (tokenizer.nextToken()) {
                        case "login":
                            event = new AccountLoginWindow();
                            ((AccountLoginWindow) event).setServerAddress(tokenizer.nextToken());
                            ((AccountLoginWindow) event).setUsername(tokenizer.nextToken());
                            ((AccountLoginWindow) event).setPassword(tokenizer.nextToken());
                            break;
                        case "newaccount":
                            event = new NewAccountWindow();
                            ((NewAccountWindow) event).setServerAddress(tokenizer.nextToken());
                            ((NewAccountWindow) event).setUsername(tokenizer.nextToken());
                            ((NewAccountWindow) event).setPassword(tokenizer.nextToken());
                            ((NewAccountWindow) event).setEmail(tokenizer.nextToken());
                            break;
                        case "anonymouslogin":
                            event = new AnonymousLoginWindow();
                            ((AnonymousLoginWindow) event).setServerAddress(tokenizer.nextToken());
                            break;
                        case "forgotpassword":
                            event = new ForgotPasswordWindow();
                            ((ForgotPasswordWindow) event).setServerAddress(tokenizer.nextToken());
                            ((ForgotPasswordWindow) event).setNewPassword(tokenizer.nextToken());
                            ((ForgotPasswordWindow) event).setRepeatPassword(tokenizer.nextToken());
                            ((ForgotPasswordWindow) event).setEmail(tokenizer.nextToken());
                            break;
                        case "help":case"?":case"h":
                            System.out.print(COMMANDS);
                            break;
                        case "shutdown":
                            event = new ShutDown();
                            if(tokenizer.hasMoreTokens()){
                                if(tokenizer.nextToken().equalsIgnoreCase("err")){
                                    ((ShutDown) event).setErrExit(true);
                                }
                            }
                            break;
                        case "forum":
                            //todo
                            break;
                        case "disconnect":
                            event = new Disconnect();
                            break;
                        case "getalltopics":
                            event=new GetAllTopics();
                            break;
                        case "gettopics":
                            event=new GetTopics();
                            break;
                        case "subscribe":
                            event=new Subscribe();
                            ((Subscribe) event).setTopicName(tokenizer.nextToken());
                            break;
                        case "unsubscribe":
                            event=new UnSubscribe();
                            ((UnSubscribe) event).setTopicName(tokenizer.nextToken());
                            break;
                        case "publish":
                            event=new Publish();
                            ((Publish) event).setTopicName(tokenizer.nextToken());
                            ((Publish) event).setTitle(tokenizer.nextToken());
                            ((Publish) event).setText(tokenizer.nextToken());
                            break;
                        default:
                            System.out.println("Unknown command:\"" + string + "\"");
                            event=null;
                            break;

                    }
                    if(event!=null){
                        uscita=true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while(!uscita);
        return event;
    }




    private void printForum(){

    }




    private void findServerOnLan() {
        ArrayList<String[]> servers = new ArrayList<>();
        ServerInfoRecover infoServer;
        int numServer;
        try {
            infoServer = new ServerInfoRecover();
        } catch (IOException e) {
            System.out.println("No server visible in the local network");
            return;
        }
        try {
            while (true) {
                servers.add(infoServer.getServerInfo());
            }
        } catch (IOException e) {
            numServer = servers.size();
            if(numServer == 0){
                System.out.println("No server visible in the local network");
                return;
            }
        }
        System.out.println("Find "+numServer+" server.");
        for (int i = 0; i<numServer;i++){
            System.out.println(i+") "+servers.get(numServer)[2]);
        }
    }



    private void printTopics(String[] topics){
        System.out.println("TOPICS LIST----------------------------------");
        if(topics!=null) {
            for (String topic : topics) {
                System.out.println(topic);
            }
        }
        System.out.println("---------------------------------------------");
    }

    /*patorjk.com*/
    private static final String ASCIIART=
            "                 ___       ______     _______.                  \n" +
            "                /   \\     /      |   /       |                  \n" +
            "               /  ^  \\   |  ,----'  |   (----`                  \n" +
            "              /  /_\\  \\  |  |        \\   \\                      \n" +
            "             /  _____  \\ |  `----.----)   |                     \n" +
            "            /__/     \\__\\ \\______|_______/                      \n" +
            "                                                                \n" +
            "           _______ .______        ______    __    __  .______   \n" +
            "          /  _____||   _  \\      /  __  \\  |  |  |  | |   _  \\  \n" +
            "         |  |  __  |  |_)  |    |  |  |  | |  |  |  | |  |_)  | \n" +
            "         |  | |_ | |      /     |  |  |  | |  |  |  | |   ___/  \n" +
            "         |  |__| | |  |\\  \\----.|  `--'  | |  `--'  | |  |      \n" +
            "          \\______| | _| `._____| \\______/   \\______/  | _|      \n" +
            "                                                                \n" +
            "            .______   .______        ______    _______          \n" +
            "            |   _  \\  |   _  \\      /  __  \\  |       \\         \n" +
            "            |  |_)  | |  |_)  |    |  |  |  | |  .--.  |        \n" +
            "            |   ___/  |      /     |  |  |  | |  |  |  |        \n" +
            "            |  |      |  |\\  \\----.|  `--'  | |  '--'  | __     \n" +
            "            | _|      | _| `._____| \\______/  |_______/ (__)    \n" +
            "                                                                \n" +
            "\n";
    private static final String PROMPT ="acsClient@Host >";

    private static  final String COMMANDS=
            "anonymouslogin <serverAddress> \n"
            +"login <serverAddress> <username> <password>\n"
            +"newaccount <serverAddress> <username> <password> <email>\n"
            +"forgotpassword <serverAddress> <newPassword> <repeatPassword> <email>\n"
            +"publish <topicName> <titolo> <testo>\n"
            +"subscribe <topicName>\nunsubscribe <topicName>\n"
            +"gettopics\ngetalltopics\ndisconnect\n" +"help/?/h\n"+"shutdown [err]\n";
}




