package utility;
//per funzionare bisogna abilitare la ricezione di messaggi UDP e tricheste di connesioni TCP per la rete locale

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class ServerInfoProvider extends InfoProviderProtocol {


    final private String serverInfoMessage;   //Stringa contente le informazioni del server(registryHost, registryPort, serverStubName)
    final private DatagramPacket packet;      //Datagram che verra inviato a tutta la rete locale con l'indirizzo della macchina su cui viene eseguito questo codice
    final private int maxError = 20;          //Numero massimo di errori che il Provider può ignorare

    private ServerSocket   serverSocket;      //Socket in ascolto sulla porta specificata nella superclasse, quelli che si connettono riceveranno le informazioni per connettersi al server (indirizzo della macchina su cui è presente il registry, porta sulla quale il registry è in ascolto, nome a cui è associato lo stub caricato sul registry)
    private DatagramSocket socket;            //Socket con cui il server trasmette il proprio indirizzo sulla porta e sul gruppo specificato nella supercalsse
    private int errorCnt = 0;                 //Contatore degli errori, protrebbero esserci delle race condition ma sono talmente poco importatnti che non vale la pena risolverle
    private Timer timer;                      //Timer usato per trasmettere in brodcast
    private boolean stop = false;          //Flag usata per fermare l'InfoProvider, viene anche attivato quando gli errori sono troppi

    private Socket clientSocket;              //Sochet del client che si è connesso al ServerSocket

    public ServerInfoProvider(String regHost, int regPort, String serverName) throws IOException {
        if (!ready)
            throw new UnknownHostException();

        byte[] buf        = "".getBytes("UTF-8");//AddressIp.getLocalAddres().getBytes("UTF-8");   //Stringa contenente l'indirizzo locale della macchina
        socket            = new DatagramSocket();
        packet            = new DatagramPacket(buf, buf.length, group, brodcastPort);
        serverSocket      = new ServerSocket(port, 5);
        serverInfoMessage = regHost + "\n" +
                regPort + "\n" +
                serverName;
        timer = new Timer();
        infoStamp("Info Provider created.");
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
            warningStamp(e, "");
            if(tooManyError() || stop){
                errorStamp(e,"Info provider has stop working.");
                timer.cancel();
                timer.purge();
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
            infoStamp("Info Provider started. Sendig the server address in brodcast on "+ brodcastPort +" port, and listening on port "+port);
        }catch (Exception e){
            errorStamp(e, "An error occurred during the start of the Info Provider.");
            stop = true;
        }
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                sendInfo();
                errorCnt=0;
            } catch (Exception e) {
                warningStamp(e, "Error during accept loop.");
                if(tooManyError() || stop){
                    errorStamp(e,"Info provider has stop working.");
                    stop = true;
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
        return errorCnt > maxError;
    }

    public void stopIt(){
        stop = true;
    }

    public boolean stopped(){
        return stop;
    }








    private void infoStamp(String msg){
        System.out.println("[InfoProvider-INFO]: " + msg);
    }

    private void warningStamp(Exception e, String msg){
        if(pedantic) {
            System.err.println("[InfoProvider-WARNING]: " + msg);
            System.err.println("\tException type: "       + e.getClass().getSimpleName());
            System.err.println("\tException message: "    + e.getMessage());
        }
    }


    private void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[InfoProvider-ERROR]: "+ msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
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

