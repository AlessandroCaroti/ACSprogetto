import client.Client;
import utility.ServerInfoRecover;

public class clientTest_3 {
    public static void main(String[] args) {
        try {
            Client client = new Client("user_2", "password", "email2");
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


            client.publish("PIPPO", "A spasso","in cerca di coca");
            System.out.println("SLEEP 9 sec.");
            Thread.sleep(9000);
            client.publish("PIPPO", "A casa","con la farina");
            Thread.sleep(9000);
            System.out.println("SLEEP 9 sec.");
            client.publish("PLUTO", "A casa di pippo", "che si diverte");
            Thread.sleep(10000);


            if(client.disconnect())
                System.out.println("DISCONNESSO");
            else {
                System.out.println("NON DISCONNESSO");
            }
            System.exit(0);

        }catch (Exception e){

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
