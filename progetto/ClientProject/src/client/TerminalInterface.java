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
        boolean uscita=false;
        Event current;
        System.out.print(ASCIIART);
        while(!uscita){
            current=this.parseCommand();
            if(current instanceof ShutDown) {
                uscita = true;
            }
            this.guiToClientEngine.offer(current);
            current=this.clientEngineToGui.poll();
            if(current!=null){
                //per l'interfaccia da terminale non mi interessa che finestra aprire ...
                //Quindi semplicemente svuoto la coda riempita da clientEngine..
                //Per la gui invece bisognerà gestire l'apertura delle finestre gli errori eccetera.
                current=null;
            }

        }
        return 0;
    }




    private Event parseCommand(){
        Event event=null;
        boolean uscita=false;
        do {
            try {
                System.out.print(PROMPT);
                String string = bufferedReader.readLine();
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
                        default:
                            System.out.println("Unknown command:\"" + string + "\"");
                            break;

                    }
                    uscita=true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while(!uscita);
        return event;
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
            +"forgotpassword <newPassword> <repeatPassword> <email>\n"

                    +"disconnect\n" +"help\n"+"shutdown\n";
}
