package Events;
import utility.Message;

import static client.ClientEventsType.NEWMESSAGE;

public class NewMessage extends ClientEvent {

    private boolean err;


    private Message message;

    public NewMessage()
    {
        this.setType(NEWMESSAGE);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

}
