package server;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ListCleaner {
    final private ConcurrentSkipListMap<String, ConcurrentSkipListSet<Integer>> list;
    final private ConcurrentSkipListSet<Integer> accountOffline;
    final private Timer timer;

    public ListCleaner(ConcurrentSkipListMap<String, ConcurrentSkipListSet<Integer>> list) {
        this.list = list;
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
        for (Integer accountId : accountOffline) {


        }
        accountOffline.clear();
    }
}
