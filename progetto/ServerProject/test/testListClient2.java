import client.AnonymousClient;
import utility.infoProvider.ServerInfoRecover;

import java.rmi.RemoteException;

public class testListClient2 {

    static private final int numG1 = 10;
    static private testListCleaner.mAnonymousClient[] group1;


    public static void main(String[] args) throws Exception {
        initAll();
        connectAll();
        registerAll();
        allUserSubscribeAllTopics();
    }

    private static void initAll() throws RemoteException {

        group1 = new testListCleaner.mAnonymousClient[numG1];

        for(int i = 0; i<numG1;i++)
            group1[i] = new testListCleaner.mAnonymousClient(false);
    }

    private static void connectAll() throws Exception {
        ServerInfoRecover infoServer = new ServerInfoRecover();
        final String[] a = infoServer.getServerInfo();

        for (AnonymousClient user:group1)
            user.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
        testConnections();
    }

    private static void testConnections() throws Exception {

        for (AnonymousClient user:group1)
            if(!user.connected())
                throw new Exception("Some user is not connected!");

    }

    private static void registerAll() throws Exception {
        for (AnonymousClient user:group1)
            if(!user.register())
                throw new Exception("Some user is not registered!");
    }

    private static void allUserSubscribeAllTopics() throws Exception {
        String[] allTopic = group1[0].getTopics();
        for (String topic: allTopic){
            for (AnonymousClient user:group1)
                if(!user.subscribe(topic))
                    throw new Exception("Some user is not connected!");
        }
    }
}
