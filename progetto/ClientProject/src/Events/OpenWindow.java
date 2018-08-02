package Events;

import guiClient.WindowType;

import static java.util.Objects.requireNonNull;

public class OpenWindow implements Event {

    private WindowType window;

    public WindowType getWindowType(){
        return window;
    }

    public void setWindowType(WindowType windowType){
        this.window=requireNonNull(windowType);
    }

}
