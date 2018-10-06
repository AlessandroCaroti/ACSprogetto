package Events;

import utility.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static client.WindowType.FORUM;

public class ForumWindow extends Window {

    private boolean err;

    private ConcurrentHashMap<String,LinkedBlockingQueue<Message>> topicMessageListMap;

    public ForumWindow(){
        this.setWindowType(FORUM);
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public ConcurrentHashMap<String, LinkedBlockingQueue<Message>> getReceivedMessageList() {
        return topicMessageListMap;
    }

    public void setReceivedMessageList(ConcurrentHashMap<String, LinkedBlockingQueue<Message>> receivedMessageList) {
        this.topicMessageListMap = receivedMessageList;
    }

}
