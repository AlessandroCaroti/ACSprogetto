package utility;
//per funzionare bisogna abilitare la ricezione di messaggi UDP per la rete locale

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ServerInfoRecover extends InfoProviderProtocol {

    private MulticastSocket socket = null;
    private byte[] buf = new byte[256];


    public ServerInfoRecover() throws IOException {
        if (!ready)
            throw new UnknownHostException();
    }
    
    public void getServerInfo() throws IOException {
        String fromServer;
        InetAddress serverAddres = findServerLocalAddress();
        Socket s = new Socket(serverAddres, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
        }
        in.close();
        s.close();
    }
    
    private InetAddress findServerLocalAddress() throws IOException {
        socket = new MulticastSocket(multicastPort);
        socket.joinGroup(group);
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        socket.leaveGroup(group);
        socket.close();
        return InetAddress.getByName(received);
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
