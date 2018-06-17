package utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class InfoProviderProtocol {
    final static private int port = 6666;


    public class ServerInfoProvider extends Thread{
        private ServerSocket serverSocket;
        final private String serverInfoMessage;

        private Socket clientSocket;

        private final boolean pedantic;

        ServerInfoProvider(String regHost, int regPort, String serverName) throws IOException {
            serverSocket = new ServerSocket(port, 5);
            serverInfoMessage = regHost +"\n"+
                    regPort +"\n"+
                    serverName;
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
                    System.out.println("\tError type: "+e.getClass().getSimpleName());
                    System.out.println("\tTrying to restart ...");
                    tryToRestart();
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
                    System.err.println("[InfoProvider-WARNING]: Unable to send serverInfo.");
                    System.err.println("\tException type: "+e.getClass().getSimpleName());
                }
            }
        }

        private void tryToRestart(){
            try {
                if(serverSocket.isClosed())
                    serverSocket = new ServerSocket(port, 5);
                this.start();
            }catch (Exception e){
                System.err.println("[InfoProvider-ERROR]: InfoProvider no more active.");
                System.err.println("\tException type: " + e.getClass().getSimpleName());
            }
        }

    }


    public class CLientInfoProvider{
        private Socket socket;

        CLientInfoProvider(){
            socket = new Socket();
        }
    }
}
