import server.Server;

import java.io.IOException;

public class serverTest {
    static Server s;
    public static void main(String[] args) {
        try {
            s = new Server();
            s.start();
        }catch (Exception e){

        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
