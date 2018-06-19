import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPTest {
    EchoClient client;


    public void setup() throws SocketException, UnknownHostException {
        new EchoServer().start();
        client = new EchoClient();
    }


    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        String echo = client.sendEcho("hello server");
        System.out.println(echo);
        echo = client.sendEcho("server is working");
        System.out.println(echo);
    }


    public void tearDown() throws IOException {
        client.sendEcho("end");
        client.close();
    }

    public static void main(String[] args) throws IOException {
        UDPTest t = new UDPTest();
        t.setup();
        t.whenCanSendAndReceivePacket_thenCorrect();
        t.tearDown();
    }
}