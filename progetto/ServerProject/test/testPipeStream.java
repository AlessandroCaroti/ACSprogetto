import server.utility.StreamRedirector;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class testPipeStream {
    static OutputStream output;

    static {
        try {
            output = StreamRedirector.redirectStdIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            int i = 0;
            try {
                while (i<50) {
                    output.write("Hello world, pipe!\n".getBytes());
                    Thread.sleep(1000);
                    i++;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });


        Thread thread2 = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            String line;
            while (true) {
                line = sc.nextLine();
                System.out.println("-"+line);
            }
        });


        thread2.start();
        thread1.start();

    }
}
