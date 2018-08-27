import client.Client;
import utility.Message;
import utility.ResponseCode;
import utility.infoProvider.ServerInfoRecover;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;

import static utility.ResponseCode.Codici.R200;

public class ClientBot extends Client {

    static private String regHost;
    static private String serverName;
    static private int regPort;

    static private Timer actionMaker;

    public static void main(String[] args) throws RemoteException {
        //System.out.println("args [numBot] [regHost] [serverName] [regPort]");

        //SET NUMBER OF BOT
        int numBot = 10;
        if (args.length > 1)
            numBot = Integer.parseInt(args[0]);

        //SET THE SERVER TO CONNECT
        if ( args.length < 4)
            findServer();
        else
            setServerInfo(args[0], args[2], Integer.valueOf(args[1]));

        //CREATE AND CONNECT THE BOTS TO THE SERVER CHOSEN
        ClientBot[] bots = new ClientBot[numBot];
        for (int i = 0; i < bots.length; i++) {
            ClientBot bot = new ClientBot(i);
            bot.connect(regHost, serverName, regPort);
            bots[i] = bot;
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
        setServerInfo(serverChosen[0], serverChosen[2], Integer.parseInt(serverChosen[1]));
    }

    static private void setServerInfo(String regHost_, String serverName_, int regPort_) {
        regHost = regHost_;
        serverName = serverName_;
        regPort = regPort_;

    }










    private ClientBot(int botNumber) throws RemoteException {
        super("bot_" + String.format("%05d", botNumber), "", "n" + botNumber + "@bot.com");
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
}
