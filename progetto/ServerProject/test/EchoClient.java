import java.io.IOException;
import java.net.*;

public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public EchoClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket(4444);
        socket.setBroadcast(true);
        address = InetAddress.getByName("255.255.255.255");
    }

    public String sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4444);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        EchoClient client = new EchoClient();
        String echo = client.sendEcho("hello server");
        System.out.println(echo);
        echo = client.sendEcho("server is working");
        System.out.println(echo);
        client.sendEcho("end");
        client.close();
    }
}