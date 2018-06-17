import server.Server;

import java.io.IOException;

public class serverTest {
    static Server s;
    public static void main(String[] args) {
        try {
            s = new Server();
            s.start();
        }catch (Exception e){
            e.getClass().getSimpleName();
            e.getMessage();
            e.printStackTrace();
        }
    }
}
