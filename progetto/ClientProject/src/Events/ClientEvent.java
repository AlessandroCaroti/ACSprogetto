package Events;

import client.ClientEventsType;

import static java.util.Objects.requireNonNull;

public abstract class ClientEvent implements  Event {
    private ClientEventsType type;

    public ClientEventsType getType(){
        return type;
    }

    public void setType(ClientEventsType type){
        this.type=requireNonNull(type);
    }


}
