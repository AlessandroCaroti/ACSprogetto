package utility.infoProvider;
//per funzionare bisogna abilitare la ricezione di messaggi UDP e tricheste di connesioni TCP per la rete locale

import utility.LogFormatManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ServerInfoProvider extends InfoProviderProtocol {

    //TCP communication
    final private ServerSocket serverSocket;    //Socket in ascolto sulla porta specificata nella superclasse, quelli che si connettono riceveranno le informazioni per connettersi al server (indirizzo della macchina su cui è presente il registry, porta sulla quale il registry è in ascolto, nome a cui è associato lo stub caricato sul registry)
    final private int tcpSocketPort;            //La porta in cui è in ascolto serverSocket
    final private String serverInfoMessage;     //Stringa contente le informazioni del server(registryHost, registryPort, serverStubName)

    //UDP communication
    final private DatagramPacket packet;        //Datagram che verra inviato a tutta la rete locale con l'indirizzo della macchina su cui viene eseguito questo codice
    private DatagramSocket socket;              //Socket con cui il server trasmette il proprio indirizzo sulla porta e sul gruppo specificato nella supercalsse

    final private LogFormatManager print = new LogFormatManager("InfoProvider", true);
    final private Timer timer;                  //Timer usato per trasmettere in brodcast
    final private int maxError = 20;            //Numero massimo di errori che il Provider può ignorare
    private int errorCnt = 0;                   //Contatore degli errori, protrebbero esserci delle race condition ma sono talmente poco importatnti che non vale la pena risolverle
    private boolean stop = false;               //Flag usata per fermare l'InfoProvider, viene anche attivato quando gli errori sono troppi



    public ServerInfoProvider(String regHost, int regPort, String serverName) throws IOException {
        if (!ready)
            throw new UnknownHostException();

        timer = new Timer();

        //CREAZIONE DEL SERVER_SOCKET
        int serverSocketPort = 6000;
        boolean serverSocketCreated = false;
        ServerSocket tmpSocket = null;
        while (!serverSocketCreated) {
            try {
                tmpSocket = new ServerSocket(serverSocketPort, 5);
                serverSocketCreated = true;
            } catch (IOException ignored) {
                serverSocketPort++;
            }
        }
        serverSocket = Objects.requireNonNull(tmpSocket);
        tcpSocketPort = serverSocketPort;

        //CREAZIONE DEL MESSAGGIO DA INVIARE IN BRODCAST
        String s = String.valueOf(serverSocketPort);
        byte[] buf = s.getBytes(StandardCharsets.UTF_8);

        socket            = new DatagramSocket();
        packet            = new DatagramPacket(buf, buf.length, group, broadcastPort);

        serverInfoMessage = regHost + "\n" +
                regPort + "\n" +
                serverName;
        print.info("Info Provider created.");
    }


    //Funzione che verrà eseguita dal timer - trasmette in brodcast l'indirisso della macchina su cui viene eseguito il codice
    private void brodcastDatagram(){
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.send(packet);
            socket.close();
            errorCnt=0;
        } catch (Exception e) {
            errorCnt++;
            print.warning(e);
            if(tooManyError() || stop){
                print.error(e,"Info provider has stop working.");
                stopTimer();
                stop = true;
            }
        }
    }



    @Override
    public void run() {
        try {
            timer.schedule(new TimerTask()
            {
                @Override
                public void run() { brodcastDatagram(); }
            }, 0L, (period * 1000));
            print.info("Info Provider started. Sendig the server address in brodcast on " + broadcastPort + " port, and listening on port " + tcpSocketPort);
        }catch (Exception e){
            print.error(e, "An error occurred during the start of the Info Provider.");
            stop = true;
        }
        while (!stop) {
            try {
                //Socket del client che si è connesso al ServerSocket
                Socket clientSocket = serverSocket.accept();
                sendInfo(clientSocket);
                errorCnt=0;
            } catch (Exception e) {
                if(!(errorCnt <= maxError && stop)) {
                    print.warning(e, "Error during accept loop.");
                    if (tooManyError() || stop) {
                        print.error(e, "Info provider has stop working.");
                        stop = true;
                    }
                }
            }
        }
        stopTimer();
        print.info("Info Provider stopped.");
    }

    private void sendInfo(Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
            out.print(serverInfoMessage);
            out.flush();
            socket.close();
        } catch (Exception e) {
            print.warning(e, "Unable to send serverInfo.");
        }
    }



    private boolean tooManyError(){
        errorCnt++;
        return errorCnt > maxError;
    }

    public void stopIt(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stop = true;
    }

    private void stopTimer(){
        timer.cancel();
        timer.purge();
    }

    public boolean stopped(){
        return stop;
    }
}

