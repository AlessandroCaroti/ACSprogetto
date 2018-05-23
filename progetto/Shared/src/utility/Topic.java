package utility;


import java.util.concurrent.atomic.AtomicInteger;

public class Topic {
    private String topicName;
    private int id_topic;
    static private AtomicInteger serial_id  = new AtomicInteger();
    static boolean reachedMaxNumTopic = false;

    public Topic(String topicName) throws Exception {
        if(reachedMaxNumTopic)
            throw new Exception();
        this.topicName = topicName;
        id_topic = serial_id.incrementAndGet();
        if(id_topic == 0) {
            reachedMaxNumTopic = true;
            throw new Exception();
        }

    }

    public String getTopicName() {
        return topicName;
    }
}
