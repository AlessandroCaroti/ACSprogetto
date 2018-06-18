package utility;

import java.net.*;

class InfoProviderProtocol extends Thread{
    final static int port = 6666;
    final static int multicastPort = 4444;
    static boolean ready = false;
    final boolean pedantic = true;
    static InetAddress group;
    {
        try {
            group = InetAddress.getByName("230.3.2.1");
            ready = true;
        } catch (UnknownHostException e) {
            ready = false;
        }
    }




    void warningStamp(Exception e, String msg){
        if(pedantic) {
            System.err.println("[InfoProvider-WARNING]: " + msg);
            System.err.println("\tException type: " + e.getClass().getSimpleName());
            System.err.println("\tException message: " + e.getMessage());
        }
    }


    void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[InfoProvider-ERROR]: "      + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }
}
