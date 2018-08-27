package server;

import server_gui.ServerGuiResizable;
import server_gui.ServerStatistic;
import utility.infoProvider.ServerInfoProvider;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Host {

    private Server server;
    private boolean started;
    private ServerStatistic serverStat;
    private ServerGuiResizable gui;
    private ServerInfoProvider infoProvider = null;
    private boolean stopAll = false;

    private InputStream  fromStdOut = null;        //stream su cui ricevere la roba scritta su System.out
    private InputStream  fromStdErr = null;        //stream su cui ricevere la roba scritta su System.err
    private OutputStream toStdIn    = null;        //stream su cui scrivere per far arrivare la roba su System.in

    final private String cmdList = "***********************************************\nCOMMANDS LIST:\n\n" +
            "\t\t?/help\n" +
            "\t\tstart [server/infoProvider/all]\n" +
            "\t\tstop [server/infoProvider/gui]\n" +
            "\t\tshutdown\n" +
            "\t\tinfo\n" +
            "\t\tshow topic\n" +
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
        server = new Server(serverStat);

        initGui();
        //todo creazione di sClient
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
                        infoStamp(cmdList);
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
                    case "FATAL_ERROR":
                        fatalError_occurred(sc);
                        break;
                    default:
                        infoStamp(line + ": command not found");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();
    }




    private void startServer() {
        if (!started) {
            server.start();
            started = true;
        } else
            infoStamp(" Server already started!");
    }

    private void startInfoProvider() {
        if (infoProvider == null) {
            try {
                infoProvider = new ServerInfoProvider(server.getRegHost(), server.getRegPort(), server.getServerName());
            } catch (IOException e) {
                errStamp("Impossible to create the info provider");
                infoProvider = null;
                return;
            }
            infoProvider.start();
        } else
            infoStamp(" InfoProvider already active!");

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
                    serverStat.setGui(null);
                    try {
                        StreamRedirector.resetAllStdStreams();
                    } catch (IOException e1) { e1.printStackTrace(); }
                    errorStamp(e, "Impossible to create the graphic user interface!");
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            serverStat.setGui(null);
            errorStamp(e, "Impossible to create the graphic user interface!");
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

    private void stopGui() throws IOException {
        if (gui != null) {
            SwingUtilities.invokeLater(() -> {
                gui.setVisible(false);
                gui.dispose();
                gui = null;
            });
            StreamRedirector.resetAllStdStreams();
        }
    }

    private void shutdownServer() {
        //chiude il server e tutto ciò a cui ne è associato (GUI, infoProvider)
        infoStamp("shutdown: COMANDO NON ANCORA FINITO DI ESSERE IMPLEMNTATO");


        stopInfoProvider();
//        stop all sClient
        try {
            stopGui();
        } catch (IOException e) {
            errorStamp(e, "Impossibile ripristinare gli standard IO");
        }
        stopServer();
        server = null;
        stopAll = true;

    }


    private void showInfo() {
        String statusGui = "Graphic Interface: " + (gui != null ? "active" : "inactive");
        String statusInfoProvider = "InfoProvider: " + (infoProvider != null ? "active" : "inactive");
        infoStamp("---------------------------------------\n" +
                serverStat.getGeneralServerStat() + "\n" +
                statusGui + "\n" +
                statusInfoProvider + "\n" +
                "---------------------------------------");
    }

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
        System.err.println("A fatal error was caught in " + sc.nextLine());
        while (sc.hasNextLine())
            System.err.println(sc.nextLine());
        System.exit(1);
    }


    private void redirectStdIO() throws IOException {
        toStdIn    = StreamRedirector.redirectStdIn();
        fromStdOut = StreamRedirector.redirectStdOut();
        fromStdErr = StreamRedirector.redirectStdErr();
    }

    private boolean resetStdIO() {
        try {
            StreamRedirector.redirectStdOut();
            StreamRedirector.redirectStdIn();
            StreamRedirector.redirectStdErr();
        } catch (IOException e) {
            errorStamp(e, "Couldn't reset STDIO to this console.");
            return false;
        }
        toStdIn = null;
        fromStdErr = null;
        fromStdOut = null;

        return true;
    }






















    static private void errorStamp(Exception e, String msg) {
        System.out.flush();
        System.err.println("[SERVER-ERROR]: " + msg);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        //e.printStackTrace();
    }

    private void infoStamp(String msg) {
        System.out.println(msg);
    }

    private void errStamp(String msg) {
        System.err.println(msg);
    }
}
