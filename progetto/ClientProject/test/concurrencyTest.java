import client.AnonymousClient;
import client.Client;
import utility.ServerInfoRecover;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class concurrencyTest {

    public static   void main(String[] args) {
        ExecutorService executorService=Executors.newFixedThreadPool(1);
        tester[] testers=new tester[1];
        for(int i=0;i<1;i++){
            testers[i]=new tester();
        executorService.submit(testers[i]);
    }

    executorService.shutdown();
    }




    public static class tester implements Runnable  {
        public tester(){}
        public void run() {
            List<AnonymousClient> clients = new LinkedList<>();
            String username = "utente";
            Random rand = new Random();
            int i = 0,j, scelta;
            AnonymousClient currentClient;
            ServerInfoRecover infoServer = null;
            try {
                infoServer = new ServerInfoRecover();
                String[] a = infoServer.getServerInfo();


            //creazione account
            while (true) {
                try {
                    scelta = rand.nextInt(4) + 1;
                    switch (scelta) {
                        case 1:
                            clients.add(currentClient=new Client(
                                    username + Integer.toString(j=rand.nextInt(300)),
                                    "password",
                                    "tobeimplementedpublickey",
                                    "tobeimplementedprivatekey",
                                    "test "+"@"+Integer.toString(j)));//TODO add special email

                            currentClient.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            break;
                        case 2:
                            clients.add(currentClient=new AnonymousClient(
                                    "tobeimplementedpublickey",
                                    "tobeimplementedprivatekey"
                            ));
                            currentClient.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
                            break;
                        case 3:
                            currentClient = clients.get(rand.nextInt(clients.size() - 1));
                            if (currentClient == null) {
                                break;
                            }
                            if (currentClient instanceof Client) {
                                if (currentClient.register()) {
                                    System.out.println("REGISTRATO");
                                } else {
                                    System.out.println("NON REGISTRATO");
                                }
                            } else {
                                if (currentClient.register()) {
                                    System.out.println("REGISTRATO");
                                } else {
                                    System.out.println("NON REGISTRATO");
                                }
                            }
                            break;
                        case 4:
                            currentClient = clients.get(rand.nextInt(clients.size() - 1));
                            if (currentClient == null) {
                                break;
                            }
                            if (currentClient.disconnect()) {
                                System.out.println("DISCONNESSO");
                            } else {
                                System.out.println("NON Disconnesso");
                            }
                            break;
                        case 5:
                            currentClient = clients.get(rand.nextInt(clients.size() - 1));
                            if (currentClient == null) {
                                break;
                            }
                            if (currentClient.retrieveAccount()) {
                                System.out.println("Account recuperato");
                            } else {
                                System.out.println("NON recuperato");
                            }
                            break;
                        default:

                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//end innerclass

}

