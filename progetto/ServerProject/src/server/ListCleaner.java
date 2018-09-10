package server;

import java.util.Timer;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ListCleaner {
    final private ConcurrentSkipListMap<String, ConcurrentSkipListSet<Integer>> list;
    final private ConcurrentSkipListSet<String> topicToClean;
    final private Timer timer;

    public ListCleaner(ConcurrentSkipListMap<String, ConcurrentSkipListSet<Integer>> list) {
        this.list = list;
        topicToClean = new ConcurrentSkipListSet<>();
        timer = new Timer(true);
    }

    void topicListToClean(String topicName) {
        topicToClean.add(topicName);
    }

    void cleanList() {
        for (String topic : topicToClean) {
            ConcurrentSkipListSet<Integer> subscribers = list.get(topic);

        }
        topicToClean.clear();
    }
}
