package Events;

import static client.WindowType.ANONYMOUSLOGIN;

public class AnonymousLoginWindow extends Window {

    private String serverAddress;
    private String port;

    public AnonymousLoginWindow(){
        this.setWindowType(ANONYMOUSLOGIN);
    }



    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
