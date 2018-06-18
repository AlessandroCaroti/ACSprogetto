package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ServerInfoRecover extends InfoProviderProtocol {

    private MulticastSocket socket = null;
    private byte[] buf = new byte[256];


    public ServerInfoRecover() throws IOException {
        if(!ready)
            throw new UnknownHostException();
        String fromServer;
        socket = new MulticastSocket(brodcastPort);
        socket.joinGroup(group);
        String received;
        int i =0;
        while (true) {
            i++;
            System.out.print(i+") ");
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            received = new String(
                    packet.getData(), 0, packet.getLength());
            if ("end".equals(received)) {
                break;
            }
            System.out.println(received);
            Socket s = new Socket(InetAddress.getByName(received),port);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
            }
            s.close();
        }
        socket.leaveGroup(group);
        socket.close();
        System.out.println(received);
    }
































    public static void main(String[] args) {
        try {
            ServerInfoRecover rec = new ServerInfoRecover();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
