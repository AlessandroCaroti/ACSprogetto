package client;

import Events.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

public class TerminalInterface implements Callable<Integer> {
    private LinkedBlockingQueue<Event> clientEngineToGui;
    private LinkedBlockingQueue<Event> guiToClientEngine;
    private BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));

    public TerminalInterface(LinkedBlockingQueue<Event> clientEngineToGUI,LinkedBlockingQueue<Event> guiToClientEngine) {
        this.clientEngineToGui=clientEngineToGUI;
        this.guiToClientEngine=guiToClientEngine;
    }


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
                        default://todo da eliminare fine debugging
                            System.err.println("uknown command");
                            break;

                    }



                }else if(current instanceof )


            }catch(InterruptedException exc){
                return 1;
            }
        }
        return 0;
    }




    private Event parseCommand(){
        Event event=null;
        String string;
        boolean uscita=false;
        do {
            try {

                System.out.print(PROMPT);
                string = bufferedReader.readLine();
                System.err.println("[DEBUG-INFO]:" + string + ";\n");
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
                        case "help":
                            System.out.print(COMMANDS);
                            break;
                        case "shutdown":
                            event = new ShutDown();
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

                    +"disconnect\n" +"help\n"+"shutdown\n";
}
