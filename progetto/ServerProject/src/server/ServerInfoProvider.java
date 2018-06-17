package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerInfoProvider extends Thread{
    ServerSocket serverSocket;
    final static private int port = 6666;
    final private String serverInfoMessage;

    private Socket clientSocket;

    private final boolean pedantic;

    ServerInfoProvider( String regHost, int regPort, String serverName) throws IOException {
        serverSocket = new ServerSocket(port, 5);
        serverInfoMessage = "";

        pedantic = true;
    }

    @Override
    public void run(){
        try {
            while (true) {
                clientSocket = serverSocket.accept();
                sendInfo();
            }
        }catch (Exception e){
            if(pedantic){
                System.out.println("[InfoProvider-WARNING]: error during accept loop.");
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
            }
        }
    }

    private void sendInfo(){
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), false);
            out.print(serverInfoMessage);
            out.flush();
            clientSocket.close();
        }catch (Exception e){
            if(pedantic){
                System.out.println("[InfoProvider-WARNING]: Unable to send serverInfo.");
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
            }
        }
    }

}
