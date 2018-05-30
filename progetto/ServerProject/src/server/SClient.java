package server;

import client.Client;
import interfaces.ClientInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.concurrent.Callable;

public class SClient implements Callable<Integer> {
    private Client[] clients;
    private static final int DEFAULTCONNECTIONNUMBER=10;


    private String myUsername;
    private String plainPassword;
    private ClientInterface skeleton;
    private String myPublicKey;
    private String myPrivateKey;

    public SClient(String username, String plainPassword, ClientInterface skeleton, String bp_key, String my_private_key)
            throws RemoteException
    {

        try {
            Properties sClientSettings = new Properties();//setting del server
            FileInputStream in = new FileInputStream("config.serverSettings");
            sClientSettings.load(in);
            in.close();
            this.clients = new Client[Integer.parseInt(sClientSettings.getProperty("maxbrokerconnection"))];
        }catch(IOException exc){
            System.err.println("ERROR:unable to open or read config.serverSettings");
            this.clients=new Client[DEFAULTCONNECTIONNUMBER];
        }

        if(username==null||plainPassword==null||skeleton==null||bp_key==null||my_private_key==null)
        {
            throw new NullPointerException("passing null argument to SClient constructor");
        }
        this.myUsername=username;
        this.plainPassword=plainPassword;
        this.skeleton=skeleton;
        this.myPublicKey=bp_key;
        this.myPrivateKey=my_private_key;
    }




    public Integer call()
    {
        //INIT












        return 0;
    }


}
