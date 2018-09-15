import client.AnonymousClient;
import client.Client;
import utility.ResponseCode;
import utility.infoProvider.ServerInfoRecover;

import java.rmi.RemoteException;
import java.util.Random;

import static utility.ResponseCode.Codici.R200;

public class testListCleaner {

    static private final int numG1 = 10;
    static private mAnonymousClient[] group1;

    static private final int numG2 = 10;
    static private mClient[] group2;

    static private mClient masterClient;


    public static void main(String[] args) throws Exception {
        initAll();
        connectAll();
        registerAll();
        createSomeTopic();
        allUserSubscribeAllTopics();
        wait_();
        disconnectSomeUser();
        wait_();
        sendRandomMessage();
        wait_();
    }

    private static void initAll() throws RemoteException {

        group1 = new mAnonymousClient[numG1];
        group2 = new mClient[numG2];

        masterClient = new mClient("MasterClient","password","master@client.com" ,false );

        for(int i = 0; i<numG1;i++)
            group1[i] = new mAnonymousClient(false);

        for (int i =0;i<numG2;i++) {
            int k = (int)(Math.random()*10000);
            group2[i] = new mClient("client_" + k, "password",  k+"@client.com", false);
        }
    }

    private static void connectAll() throws Exception {
        ServerInfoRecover infoServer = new ServerInfoRecover();
        final String[] a = infoServer.getServerInfo();

        masterClient.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
        for (AnonymousClient user:group1)
            user.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
        for (Client user: group2)
            user.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
        testConnections();
    }

    private static void testConnections() throws Exception {
        if(!masterClient.connected())
            throw new Exception("Some user is not connected!");
        for (AnonymousClient user:group1)
            if(!user.connected())
                throw new Exception("Some user is not connected!");
        for (AnonymousClient user:group2)
            if(!user.connected())
                throw new Exception("Some user is not connected!");
    }

    private static void registerAll() throws Exception {
        if(!masterClient.register())
            throw new Exception("Some user is not registered!");
        for (AnonymousClient user:group1)
            if(!user.register())
                throw new Exception("Some user is not registered!");
        for (AnonymousClient user:group2)
            if(!user.register())
                throw new Exception("Some user is not registered!");
    }

    private static void createSomeTopic() throws Exception {
        int n = 10;
        for(int i = 0; i<n;i++){
            if(!masterClient.publish("topic"+String.format("%05d", i), "__", "__"))
                throw new Exception("Errore durante la pubblicazione di un messaggio");
        }
    }

    private static void allUserSubscribeAllTopics() throws Exception {
        String[] allTopic = masterClient.getTopics();
        for (String topic: allTopic){
            if(!masterClient.subscribe(topic))
                throw new Exception("Some user is not connected!");
            for (AnonymousClient user:group1)
                if(!user.subscribe(topic))
                    throw new Exception("Some user is not connected!");
            for (AnonymousClient user:group2)
                if(!user.subscribe(topic))
                    throw new Exception("Some user is not connected!");
        }
    }

    private static void disconnectSomeUser() {
        for (int i = 0;i<numG1/2;i++){
            System.out.println("Disconnect_"+i+": "+group1[i].disconnect());
        }

        for (int i = 0;i<numG2/2;i++){
            System.out.println("Disconnect_"+i+": "+group2[i].disconnect());
        }
    }

    private static void sendRandomMessage(){
        String[] topics = masterClient.getTopicSubscribed();
        Random rand = new Random();
        int n = rand.nextInt(topics.length);
        masterClient.publish(topics[n],"test" ,"***" );
    }










    static class mAnonymousClient extends AnonymousClient{

        mAnonymousClient(boolean pedantic) throws RemoteException {
            super(pedantic);
        }
        @Override
        public ResponseCode getCode(int nAttempts) {
            return new ResponseCode(R200, ResponseCode.TipoClasse.CLIENT, "-1");
        }

    }

    static class mClient extends Client{


        mClient(String username, String plainPassword, String email, boolean pedantic) throws RemoteException {
            super(username, plainPassword, email, pedantic);
        }
        @Override
        public ResponseCode getCode(int nAttempts) {
            return new ResponseCode(R200, ResponseCode.TipoClasse.CLIENT, "-1");
        }
    }

    static private void wait_(){
        System.out.println("\n***Press Enter to continue");
        try{System.in.read();}
        catch(Exception ignored){}
    }

}