import client.Client;
import utility.infoProvider.ServerInfoRecover;

public class clientTest_3 {
    public static void main(String[] args) {
        try {
            int k = (int)(Math.random()*10000);
            Client client = new Client("user_"+ k, "password", "email_"+k);
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
            client.subscribe("PIPPO");
            client.subscribe("PLUTO");

            wait_();
            client.publish("PIPPO", "A spasso","in cerca di coca");
            wait_();
            client.publish("PIPPO", "A casa","con la farina");
            wait_();
            client.publish("PLUTO", "A casa di pippo", "che si diverte");
            Thread.sleep(1000);


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

    static private void wait_(){
        System.out.println("\n***Press Enter to continue");
        try{System.in.read();}
        catch(Exception ignored){}
    }
}
