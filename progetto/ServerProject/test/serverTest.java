import server.Server;
import utility.ServerInfoProvider;


public class serverTest {
    static Server broker;
    public static void main(String[] args) {
        try {
            broker = new Server();
            //ServerInfoProvider infoProvider = new ServerInfoProvider();
            broker.start();

        }catch (Exception e){
            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
