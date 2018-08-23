package Events;
import static client.ClientEventsType.PUBLISH;

public class Publish extends ClientEvent {

    private boolean err;

    private String topicName;

    private String title;

    private String text;

    public Publish()
    {
        this.setType(PUBLISH);
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

}
