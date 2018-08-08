package server;

import server_gui.ServerGuiResizable;
import server_gui.ServerStatistic;
import utility.ServerInfoProvider;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Host_2 extends Thread {

    private Server server;
    private boolean started;
    private ServerStatistic serverStat;
    private ServerGuiResizable gui;
    private ServerInfoProvider infoProvider = null;
    private boolean stopAll = false;

    final private String cmdList = "***********************************************\nCOMMANDS LIST:\n" +
            "\t\t?/help\n" +
            "\t\tstart [server/infoProvider]\n" +
            "\t\tstop [server/infoProvider]\n" +
            "\t\tshutdown\n" +
            "\t\tshow [info/topic]\n" +
            "\t\tpedantic\n"+
            "***********************************************";


    public Host_2(Boolean useGui) throws Exception {

        //Creazione della struttura dati delle statistiche del server (utilizzate per la comunicazione con la GUI)
        serverStat = new ServerStatistic();

        //Creazione del server
        server = new Server(serverStat);

        //Creazione della gui
        if (useGui) {
            gui = initGui(serverStat);
            //redirect I/O stream
        }
    }


    private void startServer() {
        if (!started) {
            server.start();
            started = true;
        }
        else
            infoStamp(" Server already started!");
    }

    private void startInfoProvider() {
        if(infoProvider==null) {
            try {
                infoProvider = new ServerInfoProvider(server.getRegHost(), server.getRegPort(), server.getServerName());
            } catch (IOException e) {
                errStamp("Impossible to create the info provider");
                infoProvider = null;
                return;
            }
            infoProvider.start();
        }else
            infoStamp(" InfoProvider already active!");

    }

    private void stopServer() {
        //ferma il server ma non cancella nessun dato
        //il server può essere fatto ripartire
        infoStamp("COMANDO NON ANCORA IMPLEMNTATO");
        /*
        if (started) {
            //server.stop();
            //started = false;

        }
        */
    }

    private void stopInfoProvider() {
        if (infoProvider != null) {
            infoProvider.stopIt();
            infoProvider = null;
        }
    }

    private void shutdownServer() {
        //chiude il server e tutto ciò a cui ne è associato (GUI, infoProvider)
        infoStamp("COMANDO NON ANCORA IMPLEMNTATO");

//        stopInfoProvider();
//        stop GUI
//        stop all sClient
//        stopServer();
//        server.shutdown();
//        stopAll = true;
    }


    private void showInfo() {
        String statusGui = "Graphic Interface: " +  (gui!=null?"active":"inactive");
        String statusInfoProvider ="InfoProvider: " +  (infoProvider!=null?"active":"inactive");
        infoStamp(serverStat.getGeneralServerStat()+"\n"+statusGui+"\n"+statusInfoProvider);
    }

    private ServerGuiResizable initGui(ServerStatistic serverStat) {
        if (serverStat == null)
            throw new NullPointerException();
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    ServerGuiResizable frame = new ServerGuiResizable(serverStat);
                    frame.setMinimumSize(new Dimension(780, 420));
                    frame.setUndecorated(true);
                    frame.update();
                    frame.setVisible(true);
                    serverStat.setGui(frame);
                } catch (Exception e) {
                    serverStat.setGui(null);
                    errorStamp(e);
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            serverStat.setGui(null);
            errorStamp(e);
        }
        return serverStat.getGui();
    }

    private void commandExecutorLoop() {
        Scanner sc = new Scanner(System.in);
        String line;
        while (!stopAll) {
            prompt();
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
                case "shutdown":
                    shutdownServer();
                    break;
                case "shutdown now":
                    stopAll = true;
                    break;
                case "pedantic":
                    server.togglePedantic();
                    break;
                case "info":
                    showInfo();
                    break;
                case "show topic":
                    break;
                default:
                    infoStamp(line + ": command not found");
            }

        }
    }

    @Override
    public void run(){
        commandExecutorLoop();
        System.exit(0);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("args: userinterface(true/false) ");
            return;
        }

        try {
            Host_2 h = new Host_2(Boolean.parseBoolean(args[0]));
            h.start();
        } catch (Exception e) {

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }


    static private void errorStamp(Exception e) {
        System.out.flush();
        System.err.println("[SERVER-ERROR]: Impossible to create the graphic user interface!");
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void infoStamp(String msg) {
        System.out.println(msg);
    }

    private void errStamp(String msg) {
        System.err.println(msg);
    }
    private void prompt(){
        //System.out.print("\n$ ");
    }
}
