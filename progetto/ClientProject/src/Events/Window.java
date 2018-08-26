package Events;

import client.WindowType;

import static java.util.Objects.requireNonNull;

public abstract class Window implements Event {

    private WindowType window;//Pemette tramite la classe enum WindowType un migliore gestione tramite switch in ClientEngine e TerminalInterface

    public WindowType getWindowType(){
        return window;
    }

    public void setWindowType(WindowType windowType){
        this.window=requireNonNull(windowType);
    }

}
