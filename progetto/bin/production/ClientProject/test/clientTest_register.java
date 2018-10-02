import client.Client;
import utility.infoProvider.ServerInfoRecover;

public class clientTest_register {
    public static void main(String[] args) {
        try {
            int k = (int)(Math.random()*10000);
            Client client = new Client("user_" + k, "password", "email_" + k, false);
            try {
                ServerInfoRecover infoServer = new ServerInfoRecover();
                String[] a = infoServer.getServerInfo();
                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
            }catch (Exception e){
                System.out.println("No server visible in the local network");
                System.exit(1);
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

            Thread.sleep(1000);
            if (client.publish("PIPPO", "A spasso", "in cerca di coca"))
                System.out.println("PUBBLICATO NUOVO MESSAGGIO");
            else
                System.err.println("ERRORE PUBBLICAZIONE MESSAGGIO");
            Thread.sleep(1000);
            if (client.publish("PIPPO", "A casa", "con la farina"))
                System.out.println("PUBBLICATO NUOVO MESSAGGIO");
            else
                System.err.println("ERRORE PUBBLICAZIONE MESSAGGIO");
            Thread.sleep(1000);
            if (client.publish("PLUTO", "A casa di pippo", "che si diverte"))
                System.out.println("PUBBLICATO NUOVO MESSAGGIO");
            else
                System.err.println("ERRORE PUBBLICAZIONE MESSAGGIO");
            Thread.sleep(3000);
            wait_();
            int i = 0;
            while (true) {
                if (client.publish("PIPPO", "pippo_" + i, "____"))
                    System.out.println("PUBBLICATO NUOVO MESSAGGIO");
                wait_();
                if (client.publish("PLUTO", "pluto_" + i, "____"))
                    System.out.println("PUBBLICATO NUOVO MESSAGGIO");
                wait_();
                i++;
            }

/*
            if(client.disconnect())
                System.out.println("DISCONNESSO");
            else {
                System.out.println("NON DISCONNESSO");
            }
            System.exit(0);
            */

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
