import client.AnonymousClient;
import client.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ManualClientTester {
    public static void main(String[] args) {
        try{
        System.out.println("STARTING TESTER");
        String username;String password;String email;String pubKey="tobeimplememted";String privKey="tobeimplememted";
        List<AnonymousClient> clients = new LinkedList<>();
        AnonymousClient currentClient;
        AnonymousClient it;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        int scelta;


        while (true) {
            System.out.println("Cosa vuoi fare:" +
                    "1:create client and set as current" +
                    "2:create anonymousclient and set as current" +
                    "3:print client list" +
                    "4:select client"+
                    ""

            );
            scelta = Integer.parseInt(bufferRead.readLine());
            switch (scelta) {
                case 1:

                    System.out.println("Inserisci username");
                    username=bufferRead.readLine();
                    System.out.println("Inserisci password");
                    password=bufferRead.readLine();
                    System.out.println("Inserisci email");
                    email=bufferRead.readLine();
                    clients.add(currentClient=new Client(username,password,pubKey,privKey));
                    break;
                case 2:
                    System.out.println("Inserisci username");
                    username=bufferRead.readLine();
                    System.out.println("Inserisci password");
                    password=bufferRead.readLine();
                    email=null;
                    clients.add(currentClient=new AnonymousClient(username,password,pubKey));
                    break;
                case 3:
                    for(int i=0;i<clients.size();i++){
                        it=clients.get(i);
                        System.out.println("username:"+it.getUsername()+"password",it.get);
                    }


                    break;






                default:
                    System.out.println("Comando inesistente");
                    break;
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
