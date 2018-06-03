import client.Client;

public class clientTest {
    public static void main(String[] args) {
        try {

            Client client = new Client("andrea", "ciaozio", "magariunaltravolta", "stessacosa");
            System.out.println("CLIENT PRONTO");
            if(client.connect("127.0.0.1",1099)){
                System.out.println("CONNESSO");
            }else {System.out.println("NON CONNESSO");}
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
