import client.Client;


import java.io.BufferedReader;
import java.io.InputStreamReader;


public class clientTest {
    public static void main(String[] args) {
        try {
            Client client = new Client("andrea", "ciaozio","email@ahah");
            System.out.println("CLIENT PRONTO");
            System.out.println("Servername:");
            String servername;
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            servername= bufferRead.readLine();
            client.setServerInfo("localhost",1099,servername);


            if(client.connect()!=null){
                System.out.println("CONNESSO");
            }else {System.out.println("NON CONNESSO");return ;}


            if(client.register()){
                System.out.println("REGISTRATO");
            }else{System.out.println("NON REGISTRATO");return;}

            System.out.println("Invio per continuare");
            servername= bufferRead.readLine();

            if(client.disconnect()){
                System.out.println("DISCONNESSO");
            }else{System.out.println("NON Disconnesso");return;}

            if(client.retrieveAccount()){
                System.out.println("Account recuperato");
            }else{System.out.println("NON recuperato");return;}


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
