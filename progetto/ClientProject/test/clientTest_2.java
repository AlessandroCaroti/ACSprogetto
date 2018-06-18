import client.Client;
import utility.ServerInfoRecover;

public class clientTest_2 {
    static Client c;
    public static void main(String[] args) {
        try {
            c = new Client("user_1", "password","pki_pubblica", "pki_privata");
            try {
                ServerInfoRecover infoServer = new ServerInfoRecover();
                String[] a = infoServer.getServerInfo();
                System.out.println(a[0]+a[1]+a[2]);     //stampa delle info ricevute
                c.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
            }catch (Exception e){
                System.out.println("No server visible in the local network");
                e.printStackTrace();
            }

        }catch (Exception e){

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
