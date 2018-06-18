package utility;
//per funzionare bisogna abilitare la ricezione di messaggi UDP e tricheste di connesioni TCP per la rete locale

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class ServerInfoProvider extends InfoProviderProtocol {


    final private String serverInfoMessage;     //Stringa contente le informazioni del server(registryHost, registryPort, serverStubName)
    final private DatagramPacket packet;        //Datagram che verra inviato a tutta la rete locale con l'indirizzo della macchina su cui viene eseguito questo codice
    final private int maxError = 20;

    private ServerSocket serverSocket;
    private DatagramSocket socket;
    private int errorCnt = 0;
    private Timer timer;
    private boolean stopped = false;

    private Socket clientSocket;

    public ServerInfoProvider(String regHost, int regPort, String serverName) throws IOException {
        if (!ready)
            throw new UnknownHostException();

        byte[] buf        = AddressIp.getLocalAddres().getBytes("UTF-8");   //Stringa contenente l'indirizzo locale della macchina
        socket            = new DatagramSocket();
        packet            = new DatagramPacket(buf, buf.length, group, multicastPort);
        serverSocket      = new ServerSocket(port, 5);
        serverInfoMessage = regHost + "\n" +
                regPort + "\n" +
                serverName;
        System.out.println(serverInfoMessage);
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run() { multicastDatagram(); }
        }, 0L, (period * 1000));
    }


    private void multicastDatagram(){
        try {
            System.out.print("-");
            socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
            errorCnt=0;
        } catch (Exception e) {
            errorCnt++;
            if(tooManyError() || stopped){
                errorStamp(e,"Info provider has stopped working.");
                timer.cancel();
                timer.purge();
                stopped = true;
            }
        }
    }



    @Override
    public void run() {
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                sendInfo();
                errorCnt=0;
            } catch (Exception e) {
                warningStamp(e, "Error during accept loop.");
                if(tooManyError() || stopped){
                    errorStamp(e,"Info provider has stopped working.");
                    stopped = true;
                    break;
                }
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
            warningStamp(e, "Unable to send serverInfo.");
        }
    }

    private boolean tooManyError(){
        errorCnt++;
        if(errorCnt>maxError) {
            return true;
        }
        return false;
    }




















    public static void main(String[] args) {
        try {
            ServerInfoProvider pr = new ServerInfoProvider("regHost", -1,"serverName");
            pr.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

