import client.Client;

public class clientTest_2 {
    static Client c;
    public static void main(String[] args) {
        try {
            c = new Client("user_1", "password","pki_pubblica", "pki_privata");

            if(c.connect("localhost","ServerInterface",1099)){
                System.out.println("CONNESSO");
            }else {System.out.println("NON CONNESSO");}

            if(c.register()){
                System.out.println("REGISTRATO");
            }else{System.out.println("NON REGISTRATO");}
        }catch (Exception e){

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
