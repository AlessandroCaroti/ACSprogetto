package server;

import client.Client;
import interfaces.ClientInterface;
import interfaces.ServerInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

public class SClient extends Client {
    private ServerInterface[] brokersStubs;
    private static final int DEFAULTCONNECTIONNUMBER=10;

    public SClient(String username, String plainPassword, ClientInterface skeleton, String bp_key, String my_private_key)
            throws RemoteException
    {
        super(username,plainPassword,skeleton,bp_key,my_private_key);

        try {
            Properties sClientSettings = new Properties();//setting del server
            FileInputStream in = new FileInputStream("config.serverSettings");
            sClientSettings.load(in);
            in.close();
            this.brokersStubs = new ServerInterface[Integer.parseInt(sClientSettings.getProperty("maxbrokerconnection"))];
        }catch(IOException exc){
            System.err.println("ERROR:unable to open or read config.serverSettings");
            this.brokersStubs=new ServerInterface[DEFAULTCONNECTIONNUMBER];
        }





    }


















}
