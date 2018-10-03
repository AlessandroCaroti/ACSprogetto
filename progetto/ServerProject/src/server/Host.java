package server;

import Events.NewAccountWindow;
import server.utility.StreamRedirector;
import server_gui.ServerGuiResizable;
import server_gui.ServerStatistic;
import utility.ServerInfo;
import utility.infoProvider.ServerInfoProvider;
import utility.infoProvider.ServerInfoRecover;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Host {

    final private Server server;
    final private ServerStatistic serverStat;
    final private SClient sClient;
    private ServerInfoProvider infoProvider = null;
    private ServerGuiResizable gui;

    private boolean stopAll = false;
    private boolean started;


    private InputStream  fromStdOut = null;        //stream su cui ricevere la roba scritta su System.out
    private InputStream  fromStdErr = null;        //stream su cui ricevere la roba scritta su System.err
    private OutputStream toStdIn    = null;        //stream su cui scrivere per far arrivare la roba su System.in

    final private String cmdList = "***********************************************\nCOMMANDS LIST:\n\n" +
            "\t\t?/help\n" +
            "\t\tstart [server/infoProvider/all]\n" +
            "\t\tstop [server/infoProvider/gui]\n" +
            "\t\tforce clean\n"+
            "\t\tshutdown\n" +
            "\t\tinfo\n" +
            "\t\tshow topic\n" +
            "\t\tadd broker\n"+
            "***********************************************";





    public static void main(String[] args) {
        boolean useGui = false;
        if (args.length > 0)
            useGui = Boolean.parseBoolean(args[0]);
        try {
            Host h = new Host(useGui);
            h.commandExecutorLoop();
        } catch (Exception e) {

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }






    private Host(Boolean useGui) throws Exception {

        //Creazione della struttura dati delle statistiche del server (utilizzate per la comunicazione con la GUI)
        serverStat = new ServerStatistic();

        //Creazione della gui
        if (useGui) {
            redirectStdIO();
            gui = startGui(serverStat);
        }

        //Creazione del server
        server = new Server(serverStat, true);

        //Inizializzazione della gui se necessario
        initGui();

        //Creazione interconnessione tra server
        sClient = new SClient(null , server, true);
        sClient.init();
    }



    private void commandExecutorLoop() {
        Scanner sc = new Scanner(System.in);
        String line;
        try {
            while (!stopAll) {
                line = sc.nextLine();
                switch (line) {
                    case "?":
                    case "help":
                        printCmdList();
                        break;
                    case "start":
                    case "start server":
                        startServer();
                        break;
                    case "start all":
                        startServer();
                        startInfoProvider();
                        break;
                    case "start infoProvider":
                        startInfoProvider();
                        break;
                    case "stop":
                    case "stop server":
                        stopServer();
                        break;
                    case "stop infoProvider":
                        stopInfoProvider();
                        break;
                    case "stop gui":
                        stopGui();
                        break;
                    case "force clean":
                        clean();
                        break;
                    case "shutdown":
                        shutdownServer();
                        break;
                    case "shutdown now":
                        stopAll = true;
                        break;
                    case "info":
                        showInfo();
                        break;
                    case "show topic":
                        showTopic(server.getTopicList());
                        break;
                    case "add broker":
                        System.out.println("Vuoi cercarlo nella Lan[Y/N]?");
                        switch (sc.nextLine()){
                            case"Y":
                            case "y":
                                addBroker(sc);
                                break;
                            case "N":
                            case "n":
                                System.out.print("Insert brokerName:");
                                String serverName = sc.nextLine();
                                System.out.print("Insert registry ip:");
                                String regHost = sc.nextLine();
                                System.out.print("Insert registry port:");
                                int port = Integer.parseInt(sc.nextLine());
                                addBroker(regHost,port,serverName);
                                break;
                            default:
                                System.out.println("Opzione non valida");
                        }
                        break;
                    case "FATAL_ERROR":
                        fatalError_occurred(sc);
                        break;
                    default:
                        System.out.println(line + ": command not found");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();
    }

    private void addBroker(Scanner sc){
        if(!started)
            return;
        ServerInfoRecover infoServer = null;
        try {
            infoServer = new ServerInfoRecover(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] serverChosen = infoServer.pickServerOnLan(sc);
        if(serverChosen==null)
            return;
        sClient.addServer(serverChosen[0], Integer.parseInt(serverChosen[1]), serverChosen[2]);
    }

    private void addBroker(String regHost, int regPort, String serverName){
        sClient.addServer(regHost, regPort, serverName);
    }




    private void startServer() {
        if (!started) {
            server.start();
            started = true;
        } else
            System.out.println(" Server already started!");
    }

    private void startInfoProvider() {
        if (infoProvider == null) {
            try {
                infoProvider = new ServerInfoProvider(server.getRegHost(), server.getRegPort(), server.getServerName());
            } catch (IOException e) {
                System.err.println("Impossible to create the info provider");
                infoProvider = null;
                return;
            }
            infoProvider.start();
        } else
            System.out.println(" InfoProvider already active!");

    }

    private ServerGuiResizable startGui(ServerStatistic serverStat) {
        if (serverStat == null)
            throw new NullPointerException();
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    ServerGuiResizable frame = new ServerGuiResizable(serverStat, toStdIn, fromStdOut, fromStdErr);
                    frame.setMinimumSize(new Dimension(780, 420));
                    frame.setUndecorated(true);
                    frame.update();
                    frame.setVisible(true);
                    serverStat.setGui(frame);
                } catch (Exception e) {
                    guiCreationError(e);
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            guiCreationError(e);
        }
        return serverStat.getGui();
    }

    private void initGui(){
        if(gui == null)
            return;
        gui.setServerName();
    }


    private void stopServer() {
        //ferma il server ma non cancella nessun dato
        //il server può essere fatto ripartire

        if (started) {
            server.stop();
            serverStat.setServerOffline();
            started = false;
        }
    }

    private void stopInfoProvider() {
        if (infoProvider != null) {
            infoProvider.stopIt();
            infoProvider = null;
        }
    }

    private void stopGui() {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> {
                gui.setVisible(false);
                gui.dispose();
                gui = null;
            });
            StreamRedirector.resetAllStdStreams();
        }
    }

    /**
     * chiude il server e tutto ciò a cui è associato (GUI, infoProvider, sClient)
     */
    private void shutdownServer() {
        stopInfoProvider();
        //todo stopSClient();
        stopGui();
        stopServer();
        stopAll = true;

    }

    /**
     * Stampa le informazioni relative a server, GUI, infoProvider e sClient
     */
    private void showInfo() {
        String statusGui = "Graphic Interface: " + (gui != null ? "active" : "inactive");
        String statusInfoProvider = "InfoProvider: " + (infoProvider != null ? "active" : "inactive");
        System.out.println("---------------------------------------\n" +
                serverStat.getGeneralServerStat() + "\n" +
                statusGui + "\n" +
                statusInfoProvider + "\n" +
                "---------------------------------------");
    }

    /**
     * Stampa della lista passata
     *
     * @param topicList la lista di tutti i topic presenti sul server
     */
    private void showTopic(String[] topicList) {
        if (topicList == null || topicList.length == 0) {
            System.out.println("No topics on the server");
            return;
        }
        System.out.println("---------------------------------\nTopic list:");
        for (String topic : topicList)
            System.out.println("\t" + topic);
        System.out.println("---------------------------------");
    }

    private void fatalError_occurred(Scanner sc) {
        System.err.print("A fatal error was caught in ");
        while (sc.hasNextLine())
            System.err.println(sc.nextLine());
        System.exit(1);
    }

    /**
     * Redireziona lo stdIn, stdOut e stdErr per permettere il dialogo tra GUI e server
     * @throws IOException
     */
    private void redirectStdIO() throws IOException {
        toStdIn    = StreamRedirector.redirectStdIn();
        fromStdOut = StreamRedirector.redirectStdOut();
        fromStdErr = StreamRedirector.redirectStdErr();
    }

    /**
     * Stampa dei comandi
     */
    private void printCmdList() {
        System.out.println(cmdList);
    }

    /**
     * Gestische il fallimento della creazione della GUI
     * e riporta gli stream a quelli di base(dovrebbero essere quelli associati alla console)
     *
     * @param e eccezione lanciata durante il fallimento della creazione della gui
     */
    private void guiCreationError(Exception e) {
        serverStat.setGui(null);
        StreamRedirector.resetAllStdStreams();

        System.err.println("[SERVER-ERROR]: Impossible to create the graphic user interface!");
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    private void clean(){
        server.clean();
        System.gc();
    }

}
