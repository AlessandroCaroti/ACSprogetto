package utility.infoProvider;
//per funzionare bisogna abilitare la ricezione di messaggi UDP per la rete locale
/*
  WINDOWS :   New-NetFirewallRule  -Name ASC_client -DisplayName "ACS_client" -Enable True -Profile Domain, Private  -Direction Inbound -Action Allow -Protocol "UDP" -LocalPort "4444"
 */

import utility.LogFormatManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerInfoRecover extends InfoProviderProtocol {

    private byte[] buf = new byte[256];             //Buffer su cui viene salvato il messaggio ricevuto dal brodcast
    final private LogFormatManager print = new LogFormatManager("InfoRecover", true);


    public ServerInfoRecover() throws IOException {
        if (!ready)
            throw new UnknownHostException();
    }


    public String[] getServerInfo() throws IOException {
        DatagramPacket packet = findServerLocalAddress();

        //Estrazione delle informazioni dal pacchetto ricevuto in brodcast
        InetAddress serverAddress = packet.getAddress();
        int tcpPort = Integer.parseInt(new String(Arrays.copyOf(packet.getData(), packet.getLength()), StandardCharsets.UTF_8));

        //Connesione al socket tcp da cui si riceverà le informazioni per poter connetersi al server
        return getServerInfo(serverAddress, tcpPort);
    }

    public String[] getServerInfo(InetAddress serverAddress, int tcpPort) throws IOException {
        ArrayList<String> serverInfo = new ArrayList<>();       //Array che conterra i dati per accedere allo stub del server (se tutto va bene)
        String fromServer;
        Socket s = new Socket(serverAddress, tcpPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while ((fromServer = in.readLine()) != null) {
            print.info("Received: "+fromServer);
            serverInfo.add(fromServer);
        }
        in.close();
        s.close();
        return serverInfo.toArray(new String[0]);
    }

    private Integer timeOut = super.timeOut;

    //Funzione che crea un socket in ascolto di datagram da parte di un server nella rete locale
    private DatagramPacket findServerLocalAddress() throws IOException {
        DatagramSocket socket = new MulticastSocket(broadcastPort);        //Socket in cui si riceverà l'ip del server
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        print.info("Network scan searching for a server ...");
        socket.setSoTimeout(this.timeOut*1000);                                  //Se dopo un tot di tempo nessun messaggio viene ricevuto significa che nessun sta trasmettendo
        socket.receive(packet);
        socket.close();
        print.info("Server found.");
        return packet;
    }



    public void setTimeOut(int second){
        this.timeOut = second;
    }
}
