import server.Server;
import server_gui.ServerGuiResizable;
import server_gui.ServerStatistic;
import utility.infoProvider.ServerInfoProvider;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class testGUI {

    static private Server server;
    static private ServerStatistic serverStat;
    static private ServerGuiResizable gui;


    public static void main(String[] args) {
        try {
            //Creazione della struttura dati delle statistiche del server (utilizzate per la comunicazione con la GUI)
            serverStat = new ServerStatistic();

            //Creazione del server
            server = new Server(serverStat);

            //Creazione della gui
            if(Boolean.parseBoolean(args[0]))
                gui = initGui(serverStat);

            //Attivazione del server
            server.start();

            //Creazione e esecuzione del provider delle info per potersi connetere al server (visibile sollo all'interno della rete locale)
            ServerInfoProvider infoProvider = new ServerInfoProvider(server.getRegHost(), server.getRegPort(), server.getServerName());
            infoProvider.start();

        } catch (Exception e) {
            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }



    }










    static private ServerGuiResizable initGui(ServerStatistic serverStat) {
        if (serverStat == null)
            throw new NullPointerException();
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    ServerGuiResizable frame = new ServerGuiResizable(serverStat, System.out, null, null);
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















    static private void errorStamp(Exception e){
        System.out.flush();
        System.err.println("[SERVER-ERROR]: Impossible to create the graphic user interface!");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }
}
