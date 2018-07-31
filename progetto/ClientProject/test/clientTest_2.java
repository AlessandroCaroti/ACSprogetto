import client.Client;
import utility.ServerInfoRecover;

public class clientTest_2 {
    static Client client;
    public static void main(String[] args) {
        try {
            client = new Client("user_1", "password","email");
            try {
                ServerInfoRecover infoServer = new ServerInfoRecover();
                String[] a = infoServer.getServerInfo();
                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
            }catch (Exception e){
                System.out.println("No server visible in the local network");
                e.printStackTrace();
                return;
            }
            if(client.connected()){
                System.out.println("CONNESSO");
            }
            else {
                System.out.println("NON CONNESSO");
            }
            if(client.register()){
                System.out.println("REGISTRATO");
            }else{
                System.out.println("NON REGISTRATO");
            }

        }catch (Exception e){

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
