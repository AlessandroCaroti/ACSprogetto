package utility;

import java.security.NoSuchAlgorithmException;

public class Topic {
    final private String name_topic;
    final private StringHashed id_topic;

    public Topic(String topicName) throws NoSuchAlgorithmException {
        if(topicName==null)
        {
            name_topic="UNKNOWN_TOPIC";
        }
        else{
            name_topic=topicName;
        }
        id_topic = new StringHashed(topicName);
    }

    public String getTopicName() {
        return name_topic;
    }
    public byte[] getId_topic(){return id_topic.getStringHashed();}
}
