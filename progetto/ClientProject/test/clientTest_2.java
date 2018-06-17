import client.Client;

public class clientTest_2 {
    static Client c;
    public static void main(String[] args) {
        try {
            c = new Client("asdfghj", "asdfghj", "asdfghj");
            System.out.println("client creato");
            c.connect("localhost","ServerInterface", 1099);
            System.out.println("Connesione al server avvenuta");
        }catch (Exception e){

            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
