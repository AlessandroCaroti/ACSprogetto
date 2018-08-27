import server.Server;
import utility.infoProvider.ServerInfoProvider;



public class serverTest {
    static Server broker;
    public static void main(String[] args) {
        try {
            broker = new Server();
            broker.start();

            ServerInfoProvider infoProvider = new ServerInfoProvider(broker.getRegHost(),broker.getRegPort(),broker.getServerName());
            infoProvider.start();


        }catch (Exception e){
            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
