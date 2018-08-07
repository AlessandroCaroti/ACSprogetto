import server.Server;
import utility.ServerInfoProvider;

public class testGUI {
    public static void main(String[] args) {
        try {
            Server broker = new Server(true);
            broker.start();

            ServerInfoProvider infoProvider = new ServerInfoProvider(broker.getRegHost(), broker.getRegPort(), broker.getServerName());
            infoProvider.start();


        }catch (Exception e){
            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
