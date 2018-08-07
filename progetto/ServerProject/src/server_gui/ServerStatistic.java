package server_gui;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerStatistic {

    final private String  serverName;
    //final private String  topicList;
    final private ConcurrentLinkedQueue<String> topicList;
    private AtomicInteger topicNumber;
    private AtomicInteger postNumber;
    private AtomicInteger clientNumber;
    private boolean active;


    public ServerStatistic(String serverName, ConcurrentLinkedQueue<String> topicList){
        this.serverName = Objects.requireNonNull(serverName);
        this.topicList  = Objects.requireNonNull(topicList);
        topicNumber  = new AtomicInteger();
        postNumber   = new AtomicInteger();
        clientNumber = new AtomicInteger();
        active       = false;
    }


    //SETTER
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

    public String getGeneralServerStat() {
        return  "ServerName: "      + getServerName()   +
                "\nServer ready: "  + getServerReady()  +
                "\nClient online: " + getClientNumber() +
                "\nTopic number: "  + getTopicNumber()  +
                "\nPost created: "  + getPostNumber();
    }

}
