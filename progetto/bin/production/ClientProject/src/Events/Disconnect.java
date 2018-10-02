package Events;
import static client.ClientEventsType.DISCONNECT;

public class Disconnect extends ClientEvent {

    private boolean errExit;

    public Disconnect()
    {
        this.setType(DISCONNECT);
    }

    public boolean isErrExit() {
        return errExit;
    }

    public void setErrExit(boolean errExit) {
        this.errExit = errExit;
    }

}
