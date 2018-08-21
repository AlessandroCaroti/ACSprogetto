package utility;
//per funzionare bisogna abilitare la ricezione di messaggi UDP per la rete locale
/*
  WINDOWS :   New-NetFirewallRule  -Name ASC_client -DisplayName "ACS_client" -Enable True -Profile Domain, Private  -Direction Inbound -Action Allow -Protocol "UDP" -LocalPort "4444"
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class ServerInfoRecover extends InfoProviderProtocol {

    private byte[] buf = new byte[256];             //Buffer su cui viene salvato il messaggio ricevuto dal brodcast
    final private LogFormatManager print = new LogFormatManager("InfoRecover", true);


    public ServerInfoRecover() throws IOException {
        if (!ready)
            throw new UnknownHostException();
    }


    public String[] getServerInfo() throws IOException {
        ArrayList<String> serverInfo = new ArrayList<>();       //Array che conterra i dati per accedere allo stub del server (se tutto va bene)
        String fromServer;
        InetAddress serverAddres = findServerLocalAddress();
        Socket s = new Socket(serverAddres, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while ((fromServer = in.readLine()) != null) {
            infoStamp("Receved: "+fromServer);
            serverInfo.add(fromServer);
        }
        in.close();
        s.close();
        return serverInfo.toArray(new String[0]);
    }

    public String[] getServerInfo(InetAddress serverAddres) throws IOException {
        ArrayList<String> serverInfo = new ArrayList<>();
        String fromServer;
        Socket s = new Socket(serverAddres, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while ((fromServer = in.readLine()) != null) {
            infoStamp("Receved: "+fromServer);
            serverInfo.add(fromServer);
        }
        in.close();
        s.close();
        return serverInfo.toArray(new String[0]);
    }



    //Funzione che crea un socket in ascolto di datagram da parte di un server nella rete locale
    private InetAddress findServerLocalAddress() throws IOException {
        DatagramSocket socket = new MulticastSocket(broadcastPort);        //Socket in cui si riceverà l'ip del server
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        infoStamp("Network scan searching for a server ...");
        socket.setSoTimeout(timeOut*1000);                                  //Se dopo un tot di tempo nessun messaggio viene ricevuto significa che nessun sta trasmettendo
        socket.receive(packet);
        socket.close();
        infoStamp("Server found.");
        return packet.getAddress();
    }


    private void infoStamp(String msg){
        System.out.println("[InfoRecover-INFO]: " + msg);
    }

    private void warningStamp(Exception e, String msg){
        if(pedantic) {
            System.err.println("[InfoRecover-WARNING]: " + msg);
            System.err.println("\tException type: "      + e.getClass().getSimpleName());
            System.err.println("\tException message: "   + e.getMessage());
        }
    }


    private void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[InfoRecover-ERROR]: " + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }





























    public static void main(String[] args) {
        try {
            ServerInfoRecover rec = new ServerInfoRecover();
            rec.getServerInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
