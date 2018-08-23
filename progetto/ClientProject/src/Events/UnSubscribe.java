package Events;
import static client.ClientEventsType.UNSUBSCRIBE;

public class UnSubscribe extends ClientEvent {

    private boolean err;

    private String topicName;

    public UnSubscribe()
    {
        this.setType(UNSUBSCRIBE);
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
