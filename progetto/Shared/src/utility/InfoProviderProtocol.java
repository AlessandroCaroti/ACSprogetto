package utility;

import java.io.IOException;
import java.net.*;

class InfoProviderProtocol extends Thread{
    final static int port = 6666;
    final static int brodcastPort = 4444;
    static boolean ready = false;
    static InetAddress group;
    {
        try {
            group = InetAddress.getByName("230.3.2.1");
            ready = true;
        } catch (UnknownHostException e) {
            ready = false;
        }
    }
}
