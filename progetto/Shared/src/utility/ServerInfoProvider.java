package utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class ServerInfoProvider extends InfoProviderProtocol {

    private ServerSocket serverSocket;
    final private String serverInfoMessage;
    final private DatagramPacket packet;
    private DatagramSocket socket;
    private String localIP;
    private Timer timer;
    final private byte[] buf;

    private Socket clientSocket;

    private final boolean pedantic;

    public ServerInfoProvider(String regHost, int regPort, String serverName) throws IOException {
        if (!ready)
            throw new UnknownHostException();

        buf = AddressIp.getLocalAddres().getBytes("UTF-8");

        pedantic = true;
        timer = new Timer();
        serverSocket = new ServerSocket(port, 5);
        socket = new DatagramSocket();
        packet = new DatagramPacket(buf, buf.length, group, brodcastPort);
        serverInfoMessage = regHost + "\n" +
                regPort + "\n" +
                serverName;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    socket = new DatagramSocket();
                    socket.send(packet);
                    socket.close();
                    System.out.println("Sent package with: \'"+new String(buf, "UTF-8")+"\'.");
                } catch (Exception e) {

                }
            }
        }, 0L, (10 * 1000));
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("\tWaiting a socket bind");
                clientSocket = serverSocket.accept();
                sendInfo();
                System.out.println("\tServer Info send.");
            }
        } catch (Exception e) {
            if (pedantic) {
                System.err.println("[InfoProvider-WARNING]: error during accept loop, InfoProvider no more active.");
                System.err.println("\tError type: " + e.getClass().getSimpleName());
            }
        }
    }

    private void sendInfo() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), false);
            out.print(serverInfoMessage);
            out.flush();
            clientSocket.close();
        } catch (Exception e) {
            if (pedantic) {
                System.err.println("[InfoProvider-WARNING]: Unable to send serverInfo.");
                System.err.println("\tException type: " + e.getClass().getSimpleName());
            }
        }
    }




















    public static void main(String[] args) {
        try {
            ServerInfoProvider pr = new ServerInfoProvider("regHost", -1,"serverName");
            pr.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

