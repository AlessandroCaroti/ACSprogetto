package server_gui;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerStatistic {

    private String  serverName;
    private ConcurrentLinkedQueue<String> topicList;
    private AtomicInteger topicNumber;
    private AtomicInteger postNumber;
    private AtomicInteger clientNumber;
    private boolean active;



    private ServerGuiResizable gui = null;


    public ServerStatistic(){
        topicNumber  = new AtomicInteger();
        postNumber   = new AtomicInteger();
        clientNumber = new AtomicInteger();
        active       = false;
    }



    //SETTER
    public void setServerInfo(String serverName, ConcurrentLinkedQueue<String> topicList){
        this.serverName = Objects.requireNonNull(serverName);
        this.topicList  = Objects.requireNonNull(topicList);
    }

    public void incrementTopicNum(){
        topicNumber.incrementAndGet();
    }

    public void incrementPostNum(){
        postNumber.incrementAndGet();
    }

    public void incrementClientNum(){
        clientNumber.incrementAndGet();
    }

    public void decrementTopicNum(){
        topicNumber.decrementAndGet();
    }

    public void decrementClientNum(){
        clientNumber.decrementAndGet();
    }

    public void setServerReady() {
        active = true;
    }

    public void setServerOffline() {
        active = false;
    }

    public void setGui(ServerGuiResizable gui) {
        this.gui = gui;
    }


    //GETTER
    public String getServerName() {
        return serverName;
    }

    public int getTopicNumber() {
        return topicNumber.get();
    }

    public String[] getTopicList() {
        return topicList.toArray(new String[0]);
    }

    public int getPostNumber() {
        return postNumber.get();
    }

    public int getClientNumber() {
        return clientNumber.get();
    }

    public boolean getServerReady() {
        return active;
    }

    public ServerGuiResizable getGui() {
        return gui;
    }

    public String getGeneralServerStat() {
        return  "ServerName: "      + getServerName()   +
                "\nServer ready: "  + getServerReady()  +
                "\nClient online: " + getClientNumber() +
                "\nTopic number: "  + getTopicNumber()  +
                "\nPost created: "  + getPostNumber();
    }

}
