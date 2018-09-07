import client.Client;
import utility.infoProvider.ServerInfoRecover;

import java.io.IOException;
import java.util.Arrays;

public class clientTest_logIn {

    static private ServerInfoRecover infoServer;

    static {
        try {
            infoServer = new ServerInfoRecover(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            int k = (int) (Math.random() * 10000);
            final String userName = "user_" + k;
            final String email = "email_" + k + "@testClient.com";
            register(userName, email);
            logIn(userName, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private void register(String name, String email) {
        try {
            Client client = new Client(name, "password", email, false);
            try {
                String[] a = infoServer.getServerInfo();
                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
            } catch (Exception e) {
                System.out.println("No server visible in the local network");
                System.exit(1);
            }
            if (client.connected()) {
                System.out.println("CONNESSO");
            } else {
                System.out.println("NON CONNESSO");
                System.exit(9);
            }
            if (client.register()) {
                System.out.println("REGISTRATO");
            } else {
                System.out.println("NON REGISTRATO");
                System.exit(9);
            }
            System.out.println(client.publish("PIPPO", "test", "---") ? "MESSAGGIO PUBBLICATO" : "ERRORE PUBBLICAZIONE MESSAGGIO");
            System.out.println(client.publish("PLUTO", "test", "---") ? "MESSAGGIO PUBBLICATO" : "ERRORE PUBBLICAZIONE MESSAGGIO");
//            client.subscribe("PIPPO");
//            client.subscribe("PLUTO");
            System.out.println("TOPIC A CUI SI E' ISCRITTI(register): " + Arrays.toString(client.getTopicSubscribed()));


            if (client.disconnect())
                System.out.println("DISCONNESSO\n\n*****************************************************************************\n\n\n\n\n");
            else {
                System.out.println("NON DISCONNESSO");
                System.exit(9);
            }
            Thread.sleep(5000);
        } catch (Exception e) {
            e.getClass().getSimpleName();
            e.getMessage();
            System.exit(2);
        }
    }

    static private void logIn(String name, String email) {
        try {
            Client client = new Client(name, "password", email, true);
            try {
                String[] a = infoServer.getServerInfo();
                client.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
            } catch (Exception e) {
                System.out.println("No server visible in the local network");
                System.exit(3);
            }
            if (client.connected()) {
                System.out.println("CONNESSO");
            } else {
                System.out.println("NON CONNESSO");
                System.exit(9);
            }
            if (client.retrieveAccount())
                System.out.println("LOGGATO CON SUCCESSO");
            else {
                System.out.println("LOGIN FALLITO");
                System.exit(9);
            }
            System.out.println("TOPIC A CUI SI E' ISCRITTI(log-in): " + Arrays.toString(client.getTopicSubscribed()));
            wait_();

            if (client.disconnect())
                System.out.println("DISCONNESSO");
            else {
                System.out.println("NON DISCONNESSO");
                System.exit(9);
            }
        } catch (Exception e) {
            e.getClass().getSimpleName();
            e.getMessage();
            System.exit(2);
        }
    }

    static private void wait_() {
        System.out.println("\n***Press Enter to continue");
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

}
