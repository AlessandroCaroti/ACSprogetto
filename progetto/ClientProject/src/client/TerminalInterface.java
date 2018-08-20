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
            if(current!=null){
                if(current instanceof ShutDown){
                    uscita=true;
                }
                this.guiToClientEngine.offer(current);
            }
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

        try {
            System.out.print(PROMPT);
            String string = bufferedReader.readLine();
            StringTokenizer tokenizer=new StringTokenizer(string,"\n\t ");
            if(tokenizer.hasMoreTokens()) {


                switch (tokenizer.nextToken()) {
                    case "login":
                        event=new AccountLoginWindow();
                        ((AccountLoginWindow) event).setServerAddress(tokenizer.nextToken());
                        ((AccountLoginWindow) event).setUsername(tokenizer.nextToken());
                        ((AccountLoginWindow) event).setPassword(tokenizer.nextToken());
                        break;
                    case "newaccount":
                        event=new NewAccountWindow();
                        ((NewAccountWindow) event).setServerAddress(tokenizer.nextToken());
                        ((NewAccountWindow) event).setUsername(tokenizer.nextToken());
                        ((NewAccountWindow) event).setPassword(tokenizer.nextToken());
                        ((NewAccountWindow) event).setEmail(tokenizer.nextToken());
                        break;
                    case "anonymouslogin":
                        event=new AnonymousLoginWindow();
                        ((AnonymousLoginWindow) event).setServerAddress(tokenizer.nextToken());
                        break;
                    case "forgotpassword":
                        event=new ForgotPasswordWindow();
                        ((ForgotPasswordWindow) event).setNewPassword(tokenizer.nextToken());
                        ((ForgotPasswordWindow) event).setRepeatPassword(tokenizer.nextToken());
                        ((ForgotPasswordWindow) event).setEmail(tokenizer.nextToken());
                        break;
                    case "help":
                        System.out.print(COMMANDS);
                        break;
                    case "shutdown":
                        event=new ShutDown();
                        break;
                    case "forum":
                        //todo
                        break;
                    default:
                        break;


                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return event;
    }

    /*patorjk.com*/
    private static final String ASCIIART=
    "                ,.,   '                   ,. - .,                      ,. -,                                 ,.-·^*ª'` ·,             ,. -  .,                     , ·. ,.-·~·.,   ‘       .-,             ,'´¨';'           .,                                          .,                     ,. -  .,                     , ·. ,.-·~·.,   ‘        ;'*¨'`·- .,  ‘            \n" +
            "              ;´   '· .,             ,·'´ ,. - ,   ';\\            ,.·'´,    ,'\\                             .·´ ,·'´:¯'`·,  '\\‘        ,' ,. -  .,  `' ·,             /  ·'´,.-·-.,   `,'‚       ;  ';\\          ,'   ';'\\'     ,·´    '` ·.'                                ,·´    '` ·.'             ,' ,. -  .,  `' ·,             /  ·'´,.-·-.,   `,'‚        \\`:·-,. ,   '` ·.  '      \n" +
            "            .´  .-,    ';\\       ,·´  .'´\\:::::;'   ;:'\\ '     ,·'´ .·´'´-·'´::::\\'                         ,´  ,'\\:::::::::\\,.·\\'       '; '·~;:::::'`,   ';\\         /  .'´\\:::::::'\\   '\\ °    ';   ;:'\\        ,'   ,'::'\\     \\`; `·;·.   `·,                             \\`; `·;·.   `·,         '; '·~;:::::'`,   ';\\         /  .'´\\:::::::'\\   '\\ °       '\\:/   ;\\:'`:·,  '`·, '   \n" +
            "           /   /:\\:';   ;:'\\'    /  ,'´::::'\\;:-/   ,' ::;  '  ;    ';:::\\::\\::;:'                         /   /:::\\;·'´¯'`·;\\:::\\°      ;   ,':\\::;:´  .·´::\\'    ,·'  ,'::::\\:;:-·-:';  ';\\‚    ';  ';::';      ,'   ,'::::;      ;   ,'\\::`·,   \\'                            ;   ,'\\::`·,   \\'        ;   ,':\\::;:´  .·´::\\'    ,·'  ,'::::\\:;:-·-:';  ';\\‚        ;   ;'::\\;::::';   ;\\   \n" +
            "         ,'  ,'::::'\\';  ;::';  ,'   ;':::::;'´ ';   /\\::;' '    \\·.    `·;:'-·'´                           ;   ;:::;'          '\\;:·´      ;  ·'-·'´,.-·'´:::::::';  ;.   ';:::;´       ,'  ,':'\\‚   ';  ';::;     ,'   ,'::::;'      ;   ,'::'\\:::';   ';                          ;   ,'::'\\:::';   ';       ;  ·'-·'´,.-·'´:::::::';  ;.   ';:::;´       ,'  ,':'\\‚       ;  ,':::;  `·:;;  ,':'\\' \n" +
            "     ,.-·'  '·~^*'´¨,  ';::;  ;   ;:::::;   '\\*'´\\::\\'  °     \\:`·.   '`·,  '                          ';   ;::/      ,·´¯';  °      ;´    ':,´:::::::::::·´'    ';   ;::;       ,'´ .'´\\::';‚  ';  ';::;    ,'   ,'::::;'       ;   ;:::;'·:.'  ,·'\\'                         ;   ;:::;'·:.'  ,·'\\'    ;´    ':,´:::::::::::·´'    ';   ;::;       ,'´ .'´\\::';‚     ;   ;:::;    ,·' ,·':::; \n" +
            "     ':,  ,·:²*´¨¯'`;  ;::';  ';   ';::::';    '\\::'\\/.'          `·:'`·,   \\'                           ';   '·;'   ,.·´,    ;'\\         ';  ,    `·:;:-·'´         ';   ':;:   ,.·´,.·´::::\\;'°   \\   '·:_,'´.;   ;::::;‘      ';  ';: -· '´. ·'´:::'\\'                       ';  ';: -· '´. ·'´:::'\\'    ';  ,    `·:;:-·'´         ';   ':;:   ,.·´,.·´::::\\;'°     ;  ;:::;'  ,.'´,·´:::::; \n" +
            "     ,'  / \\::::::::';  ;::';   \\    '·:;:'_ ,. -·'´.·´\\‘         ,.'-:;'  ,·\\                          \\'·.    `'´,.·:´';   ;::\\'       ; ,':\\'`:·.,  ` ·.,         \\·,   `*´,.·'´::::::;·´       \\·,   ,.·´:';  ';:::';       ;  ,-·:'´:\\:::::::;·'                        ;  ,-·:'´:\\:::::::;·'     ; ,':\\'`:·.,  ` ·.,         \\·,   `*´,.·'´::::::;·´       ':,·:;::-·´,.·´\\:::::;´'  \n" +
            "    ,' ,'::::\\·²*'´¨¯':,'\\:;     '\\:` ·  .,.  -·:´::::::\\'   ,·'´     ,.·´:::'\\                          '\\::\\¯::::::::';   ;::'; ‘     \\·-;::\\:::::'`:·-.,';        \\\\:¯::\\:::::::;:·´           \\:\\¯\\:::::\\`*´\\::;  '    ,'  ';::::::'\\;:·'´                           ,'  ';::::::'\\;:·'´         \\·-;::\\:::::'`:·-.,';        \\\\:¯::\\:::::::;:·´           \\::;. -·´:::::;\\;·´     \n" +
            "    \\`¨\\:::/          \\::\\'       \\:::::::\\:::::::;:·'´'     \\`*'´\\::::::::;·'‘                           `·:\\:::;:·´';.·´\\::;'         \\::\\:;'` ·:;:::::\\::\\'       `\\:::::\\;::·'´  °              `'\\::\\;:·´'\\:::'\\'   '    \\·.,·\\;-· '´  '                              \\·.,·\\;-· '´  '             \\::\\:;'` ·:;:::::\\::\\'       `\\:::::\\;::·'´  °             \\;'\\::::::::;·´'        \n" +
            "     '\\::\\;'            '\\;'  '       `· :;::\\;::-·´           \\::::\\:;:·´                                    ¯      \\::::\\;'‚          '·-·'       `' · -':::''          ¯                                     `*´°        \\::\\:\\                                      \\::\\:\\                     '·-·'       `' · -':::''          ¯                           `\\;::-·´            \n" +
            "       `¨'                                                     '`*'´‘                                                 '\\:·´'                                            ‘                                      '            `'·;·'                                       `'·;·'                                                     ‘                                               \n" +
            "\n" +
            "\n";

    private static final String PROMPT ="acsClient@Host >";

    private static  final String COMMANDS=
            "anonymouslogin <serverAddress> \n"
            +"login <serverAddress> <username> <password>\n"
            +"newaccount <serverAddress> <username> <password> <email>\n"
            +"forgotpassword <newPassword> <repeatPassword> <email>\n"
            +"help\n"+"shutdown\n";
}
