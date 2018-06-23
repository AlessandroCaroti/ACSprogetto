import client.Client;
import utility.ServerInfoRecover;

public class clientTest_2 {
    static Client client;
    public static void main(String[] args) {
        try {
            client = new Client("user_1", "password","pki_pubblica", "pki_privata");
            try {
                ServerInfoRecover infoServer = new ServerInfoRecover();
                String[] a = infoServer.getServerInfo();
                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
            }catch (Exception e){
                System.out.println("No server visible in the local network");
                e.printStackTrace();
                return;
            }
            if(client.register()){
                System.out.println("REGISTRATO");
            }else{System.out.println("NON REGISTRATO");return;}

        }catch (Exception e){

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
