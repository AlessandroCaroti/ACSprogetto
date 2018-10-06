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
import java.util.*;

public class ServerInfoRecover extends InfoProviderProtocol {

    private byte[] buf = new byte[256];             //Buffer su cui viene salvato il messaggio ricevuto dal brodcast
    private Integer timeOut = super.timeOut * 1000;
    final private LogFormatManager print;


    public ServerInfoRecover() throws IOException {
        this(true);
    }

    public ServerInfoRecover(final boolean pedantic) throws IOException {
        if (group == null)
            throw new UnknownHostException();
        print = new LogFormatManager("InfoRecover", pedantic);
    }

    /**
     * Se esiste un infoProvider nella rete locale, recupera le informazioni necessarie per protesi
     * connettere ad un server
     *
     * @return null in caso non si trvino dati apprpriati o un array di dimensioni 3 contenente:
     * -in pos 0 l'ip dell'host del registry su cui è salvato lo stub del server
     * -in pos 1 la porta del registry
     * -il nome del server
     * @throws IOException nel caso in cui non ci siano infoProvider nella lan
     */
    public String[] getServerInfo() throws IOException {
        DatagramPacket packet = findServerLocalNetwork();

        //Estrazione delle informazioni dal pacchetto ricevuto in brodcast
        InetAddress serverAddress = packet.getAddress();
        int tcpPort = Integer.parseInt(new String(Arrays.copyOf(packet.getData(), packet.getLength()), StandardCharsets.UTF_8));

        //Connesione al socket tcp da cui si riceverà le informazioni per poter connetersi al server
        return getServerInfo(serverAddress, tcpPort);
    }

    public String[] getServerInfo (InetAddress serverAddress) throws IOException{
        InetAddress packetAddress;
        DatagramPacket packet;
        do{
            packet=findServerLocalNetwork();
        }while(!((packetAddress=packet.getAddress()).toString().equals(serverAddress.toString())));//todo check se si può eliminare la connversione a strimga e usare subito equals()
        int tcpPort = Integer.parseInt(new String(Arrays.copyOf(packet.getData(), packet.getLength()), StandardCharsets.UTF_8));
        return getServerInfo(packetAddress, tcpPort);
    }

    public String[] getServerInfo(InetAddress serverAddress, int tcpPort) throws IOException {
        ArrayList<String> serverInfo = new ArrayList<>();       //Array che conterra i dati per accedere allo stub del server (se tutto va bene)
        String fromServer;
        Socket s = new Socket(serverAddress, tcpPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while ((fromServer = in.readLine()) != null) {
            print.pedanticInfo("Received: " + fromServer);
            serverInfo.add(fromServer);
        }
        in.close();
        s.close();
        String[] connectionData = serverInfo.toArray(new String[0]);
        try {
            ServerConnectionInfo.validateData(connectionData);
        } catch (IllegalArgumentException e) {
            print.pedanticWarning(e, "ServerInfo refused.");
            return null;
        }
        print.info("Found new server info(ServerName: \'" + connectionData[2] + "\').");
        return serverInfo.toArray(new String[0]);
    }

    //Funzione che crea un socket in ascolto di datagram da parte di un server nella rete locale
    private DatagramPacket findServerLocalNetwork() throws IOException {
        DatagramSocket socket = new MulticastSocket(broadcastPort);        //Socket in cui si riceverà l'ip del server
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        print.info("Network scan searching for a server ...");
        socket.setSoTimeout(this.timeOut);                             //Se dopo un tot di tempo nessun messaggio viene ricevuto significa che nessun sta trasmettendo
        socket.receive(packet);
        socket.close();
        print.pedanticInfo("... new infoProvider found.");
        return packet;
    }

    /**Permette di trovare tutti i server nella rete locale che dispongono di un infoProvider attivo.
     * A volte non li trova tutti =(
     *
     * @return
     */
    public HashMap<String, String[]> findAllServerOnLan() {
        HashMap<String, String[]> servers = new HashMap<>();
        try {
            final long start = System.currentTimeMillis();
            while (true) {  //Quando non ci sono più server diponibili viene lanciata un eccezione che rompe il ciclo while
                String[] newServer = this.getServerInfo();
                if (newServer != null) {
                    servers.putIfAbsent(newServer[2], newServer);
                }
                if (System.currentTimeMillis() - start > timeOut)
                    timeOut = 400;
            }
        } catch (IOException ignored) {
        }
        return servers;
    }

    public String[] pickServerOnLan(Scanner sc) {
        HashMap<String, String[]> servers;
        int numServer;

        //RICERCA DEI SERVER DISPONIBILI NELLA RETE LOCALE
        print.pedanticInfo("Local network scan looking for servers ...");
        servers = this.findAllServerOnLan();
        numServer = servers.size();
        if (numServer == 0) {
            print.info("... no server found on the local network!");
            return null;
        }

        //STAMPA DEI SERVER TROVATI
        print.pedanticInfo("\n\n\n\nFound " + numServer + " Servers:");
        String[] serverFound = servers.keySet().toArray(new String[0]);
        for (int i = 0; i < numServer; i++) {
            System.out.println("  " + i + ") " + serverFound[i]);
        }

        //SCELTA DEL SERVER A CUI CONNETTERSI
        int serverToConnect ;
        System.out.println("\nConnect to server number: ");
        serverToConnect = sc.nextInt();
        if(serverToConnect<0||serverToConnect>servers.size())
            print.info("Number not valid");
        return servers.get(serverFound[serverToConnect]);
    }
}
