package Events;
import static client.ClientEventsType.GETTOPICS;

public class GetTopics extends ClientEvent {

    private boolean err;
    private String[] topicsList;

    public String[] getTopicsList() {
        return topicsList;
    }

    public void setTopicsList(String[] topicsList) {
        this.topicsList = topicsList;
    }

    public GetTopics()
    {
        this.setType(GETTOPICS);
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

}