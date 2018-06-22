import server.Server;
import utility.ServerInfoProvider;

import java.net.InetAddress;


public class ServerTest2 {
    static Server broker;
    public static void main(String[] args) {
        try {
            broker = new Server();
            broker.start();
            SecurityManager security = System.getSecurityManager();


        }catch (Exception e){
            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
