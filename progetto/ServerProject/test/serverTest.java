import server.Server;
import utility.ServerInfoProvider;


public class serverTest {
    static Server broker;
    public static void main(String[] args) {
        try {
            broker = new Server();

            ServerInfoProvider infoProvider = new ServerInfoProvider(broker.getRegHost(),broker.getRegPort(),broker.getServerName());
            infoProvider.start();

            //broker.start();
        }catch (Exception e){
            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
