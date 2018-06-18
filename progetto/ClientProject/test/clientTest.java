import client.Client;

public class clientTest {
    public static void main(String[] args) {
        try {

            Client client = new Client("andrea", "ciaozio", "magariunaltravolta", "stessacosa");
            System.out.println("CLIENT PRONTO");
            /*if(client.connect("localhost","ServerInterface",1099)){
                System.out.println("CONNESSO");
            }else {System.out.println("NON CONNESSO");}
            if(client.register()){
                System.out.println("REGISTRATO");
            }else{System.out.println("NON REGISTRATO");}
            if(client.disconnect()){
                System.out.println("DISCONNESSO");
            }else{System.out.println("NON Disconnesso");}
            if(client.retrieveAccount()){
                System.out.println("Account recuperato");
            }else{System.out.println("NON recuperato");}
*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
