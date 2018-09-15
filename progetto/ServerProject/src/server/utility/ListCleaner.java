package server.utility;

import account.AccountCollectionInterface;
import client.AnonymousClient;
import interfaces.ClientInterface;
import utility.LogFormatManager;

import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ListCleaner {
    final private ConcurrentSkipListMap<String, ConcurrentSkipListSet<Integer>> list;
    final private ConcurrentSkipListSet<Integer> accountOffline;
    final private AccountCollectionInterface accountList;
    final private Timer timer;
    final private AnonymousClient fakeClient;
    final private LogFormatManager print = new LogFormatManager("LIST_CLEANER", false);

    public ListCleaner(ConcurrentSkipListMap<String, ConcurrentSkipListSet<Integer>> list, AccountCollectionInterface accountList) throws RemoteException {
        Objects.requireNonNull(this.list = list);
        Objects.requireNonNull(this.accountList = accountList);

        fakeClient = new AnonymousClient();
        accountOffline = new ConcurrentSkipListSet<>();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    clean();
                }catch (Exception e){
                    print.error(e, "Cleaning list failed.");
                }
            }
        }, 15*60*1000L,5*60*1000);
    }

    public void addClientOffline(int accountId) {
        accountOffline.add(accountId);
    }

    public void clean() {
        print.info("Clean started...");
        Thread.currentThread().setPriority(4);
        ClientInterface fakeStub = fakeClient.getSkeleton();
        for (Integer accountId : accountOffline) {
            ClientInterface prev = accountList.setStub(fakeStub, accountId);
            if (prev != null)   //account in utilizzo
                continue;
            String[] topicSubscribed = accountList.getTopicSubscribed(accountId);
            for (String topic: topicSubscribed){
                list.get(topic).remove(accountId);
            }
            accountList.setStub(null, accountId, true);
            accountOffline.remove(accountId);
//            Thread.yield();
        }
        print.info("...clean finished");
    }

    public void stop(){
        clean();
        timer.cancel();
        timer.purge();
    }
}
