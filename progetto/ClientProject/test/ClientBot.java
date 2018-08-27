import client.Client;
import utility.Message;
import utility.ResponseCode;
import utility.infoProvider.ServerInfoRecover;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import static utility.ResponseCode.Codici.R200;

public class ClientBot extends Client {

    static private String regHost;
    static private String serverName;
    static private int regPort;


    public static void main(String[] args) throws RemoteException {
        //System.out.println("args [numBot] <[regHost] [serverName] [regPort]>");

        //SET NUMBER OF BOT
        int numBot = 10;
        if (args.length > 0)
            numBot = Integer.parseInt(args[0]);

        //SET THE SERVER TO CONNECT
        if ( args.length < 4)
            findServer();
        else
            setServerInfoForBot(args[0], args[2], Integer.valueOf(args[1]));

        //CREATE AND CONNECT THE BOTS TO THE SERVER CHOSEN
        ClientBot[] bots = new ClientBot[numBot];
        for (int i = 0; i < bots.length; i++) {
            ClientBot bot = new ClientBot(i);
            bot.setServerInfo(regHost,regPort, serverName);
            if(!bot.connected())
            {
                System.err.println("can not connect to the server");
                System.exit(-1);
            }
            bot.register();
            bots[i] = bot;
        }
        for(ClientBot bot : bots){
            bot.start();
        }
        
    }

    static private void findServer() {
        ArrayList<String[]> servers = new ArrayList<>();
        ServerInfoRecover infoServer;
        int numServer;

        //RICERCA DEI SERVER DISPONIBILI NELLA LAN
        try {
            infoServer = new ServerInfoRecover();
        } catch (IOException e) {
            System.err.println("ERRORE DURANTE LA CREAZIONE DELL'INFO_RECOVER");
            return;
        }
        try {
            while (true) {  //Quando non ci sono più server diponibili viene lanciata un eccezione che rompe il ciclo while
                servers.add(infoServer.getServerInfo());
                infoServer.setTimeOut(1);
            }
        } catch (IOException e) {
            numServer = servers.size();
            if (numServer == 0) {
                System.err.println("No server found available for connection.");
                System.exit(-1);
            }
        }

        //STAMPA DEI SERVER TROVATI
        System.out.println("\n\n\n\nFind " + numServer + " server:");
        for (int i = 0; i < numServer; i++) {
            System.out.println("  "+i + ") " + (servers.get(i))[2]);
        }

        //SCELTA DEL SERVER A CUI CONNETERSI
        int serverToConnect = 0;
        if (numServer != 1) {
            System.out.println("\nConnect to server number: ");
            Scanner sc = new Scanner(System.in);
            serverToConnect = sc.nextInt();
        }
        String[] serverChosen = servers.get(serverToConnect);
        setServerInfoForBot(serverChosen[0], serverChosen[2], Integer.parseInt(serverChosen[1]));
    }

    static private void setServerInfoForBot(String regHost_, String serverName_, int regPort_) {
        regHost = regHost_;
        serverName = serverName_;
        regPort = regPort_;
    }













    final private int sleepTime;
    final private Random random = new Random((long) (Math.random()*100000000));

    private ClientBot(int botNumber) throws RemoteException {
        super("bot_" + String.format("%05d", botNumber), "", "n" + botNumber + "@bot.com");

        sleepTime = 10 + random.nextInt(35);
    }

    @Override
    public boolean publish(String topic, String title, String text) {
        if (connected()) {
            try {
                Message msg = new Message(title, this.username, "", topic);
                server_stub.publish(this.cookie, msg);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public ResponseCode notify(Message m) {
        return new ResponseCode(R200, ResponseCode.TipoClasse.CLIENT, "");
    }

    @Override
    public ResponseCode getCode(int nAttempts) {
        return new ResponseCode(R200, ResponseCode.TipoClasse.CLIENT, "-1");
    }

    private void start(){
        this.publish("Sport"   , username+"_test00", null);
        this.publish("Hardware", username+"_test00", null);
        this.publish("Security", username+"_test00", null);
        this.publish("Films"   , username+"_test00", null);
        this.publish("Music"   , username+"_test00", null);

        topicsSubscribed.add("Sport");
        topicsSubscribed.add("Hardware");
        topicsSubscribed.add("Security");
        topicsSubscribed.add("Films");
        topicsSubscribed.add("Music");

        Thread thread = new Thread(() -> {
            int msgNum = 1;
            boolean flag = true;
            while (flag) {
                try {
                    Thread.sleep(sleepTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int k = random.nextInt(100);

                //5% CREARE UN NUOVO TOPIC
                if(k<5){
                    String newTopic = "Topic_" + random.nextInt(1024);
                    this.publish(newTopic, username+"_test"+msgNum, null);
                    this.subscribe(newTopic);
                    msgNum++;
                }
                //15% ISCRIVITI AD UN NUOVO TOPIC CASUALE
                else if (k < 20) {
                    try {
                        String[] allTopics = server_stub.getTopicList();
                        this.subscribe(allTopics[random.nextInt(allTopics.length)]);
                    } catch (RemoteException e) {
                        flag = false;
                    }
                }

                //60% PUBBLICA UN MESSAGGIO SU UN TOPIC CASUALE
                else if (k < 80) {
                    int topic = random.nextInt(topicsSubscribed.size());
                    Iterator<String> i = topicsSubscribed.iterator();
                    String topicName = null;
                    for (int j = 0;j!=topic;j++)
                        topicName = i.next();
                    try {
                        this.publish(topicName, username+"_test"+msgNum, null);
                        msgNum++;
                    }catch (Exception ignored){ }
                }
                //15% NON FA NULLA

            }
            System.err.println(this.username+" stop working!!!");
            this.disconnect();
        });

        thread.start();
        System.err.println(this.username+" start working.");
    }
}
