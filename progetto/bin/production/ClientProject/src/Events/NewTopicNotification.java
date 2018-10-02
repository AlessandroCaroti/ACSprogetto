package Events;
import static client.ClientEventsType.NEWTOPICNOTIFICATION;

public class NewTopicNotification extends ClientEvent {

    private boolean err;

    private String topicName;

    public NewTopicNotification()
    {
        this.setType(NEWTOPICNOTIFICATION);
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

}
