package Events;

import client.ClientEventsType;

import static java.util.Objects.requireNonNull;

public abstract class ClientEvent implements  Event {
    private ClientEventsType type;//Pemette tramite la classe enum ClienEventsType un migliore gestione tramite switch in ClientEngine e TerminalInterface

    public ClientEventsType getType(){
        return type;
    }

    public void setType(ClientEventsType type){
        this.type=requireNonNull(type);
    }


}
