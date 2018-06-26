import client.AnonymousClient;
import client.Client;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class concurrencyTest {

    public static void main(String[] args) throws RemoteException {
        List<AnonymousClient> clients = new LinkedList<>();
        String username="utente";
        Random rand=new Random();
        int i=0;

        //creazione account
        while(true){
            if(i%2==0){//account normale
                clients.add(new Client(
                        username+Integer.toString(rand.nextInt(300)),
                        "password",
                        "tobeimplementedpublickey",
                        "tobeimplementedprivatekey",
                        "test@test.com"));//TODO add special email
            }else{//anon
                clients.add(new AnonymousClient(
                        username+Integer.toString(rand.nextInt(300)),
                        "tobeimplementedpublickey",
                        "tobeimplementedprivatekey"
                ));
            }
            if(i++>=300){i=0;}
        }













    }

}
