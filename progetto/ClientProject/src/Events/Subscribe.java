package Events;
import static client.ClientEventsType.SUBSCRIBE;

public class Subscribe extends ClientEvent {

    private boolean err;

    private String topicName;

    public Subscribe()
    {
        this.setType(SUBSCRIBE);
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
