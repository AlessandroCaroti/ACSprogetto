package server.utility;

import account.AccountCollectionInterface;
import client.AnonymousClient;
import interfaces.ClientInterface;

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

    public ListCleaner(ConcurrentSkipListMap<String, ConcurrentSkipListSet<Integer>> list, AccountCollectionInterface accountList) throws RemoteException {
        Objects.requireNonNull(this.list = list);
        Objects.requireNonNull(this.accountList = accountList);

        fakeClient = new AnonymousClient();
        accountOffline = new ConcurrentSkipListSet<>();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cleanList();
            }
        }, 15*60*1000L,5*60*1000);
    }

    public void addClientOffline(int accountId) {
        accountOffline.add(accountId);
    }

    public void cleanList() {
        Thread.currentThread().setPriority(4);
        ClientInterface fakeStub = fakeClient.getSkeleton();
        for (Integer accountId : accountOffline) {
            ClientInterface prev = accountList.setStub(fakeStub, accountId);
            if (prev != null)   //account in utilizzo
                continue;
            //todo rimuovere l'account corrente dalle varie liste
            String[] topicSubscribed = accountList.getTopicSubscribed(accountId);
            for (String topic: topicSubscribed){
                list.get(topic).remove(accountId);
            }
            accountList.setStub(null, accountId, true);
//            Thread.yield();
        }
        accountOffline.clear();
    }

    public void stop(){
        cleanList();
        timer.cancel();
        timer.purge();
    }
}
