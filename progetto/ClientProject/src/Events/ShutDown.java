package Events;
import static client.ClientEventsType.SHUTDOWN;

public class ShutDown extends ClientEvent {
	
	private boolean errExit;
	
	public ShutDown() 
	{
		this.setType(SHUTDOWN);
	}

	public boolean isErrExit() {
		return errExit;
	}

	public void setErrExit(boolean errExit) {
		this.errExit = errExit;
	}
	
}
