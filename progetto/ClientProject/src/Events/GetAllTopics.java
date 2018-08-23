package Events;
import static client.ClientEventsType.GETALLTOPICS;
public class GetAllTopics extends ClientEvent {

    private boolean err;
    private String[] topicsList;

    public String[] getTopicsList() {
        return topicsList;
    }

    public void setTopicsList(String[] topicsList) {
        this.topicsList = topicsList;
    }

    public GetAllTopics()
    {
        this.setType(GETALLTOPICS);
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

}