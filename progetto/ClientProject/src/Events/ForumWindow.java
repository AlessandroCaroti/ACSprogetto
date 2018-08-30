package Events;


import utility.Message;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import static client.WindowType.FORUM;

public class ForumWindow extends Window {

    private boolean err;

    private ConcurrentMap<String,LinkedBlockingQueue<Message>> topicMessageListMap;

    public ForumWindow(){
        this.setWindowType(FORUM);
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public ConcurrentMap<String, LinkedBlockingQueue<Message>> getReceivedMessageList() {
        return topicMessageListMap;
    }

    public void setReceivedMessageList(ConcurrentMap<String, LinkedBlockingQueue<Message>> receivedMessageList) {
        this.topicMessageListMap = receivedMessageList;
    }

}
