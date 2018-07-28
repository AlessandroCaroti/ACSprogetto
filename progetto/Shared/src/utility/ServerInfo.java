package utility;

public class ServerInfo {

    public String regHost;
    public int regPort;

    public ServerInfo(String regHost,int regPort){
        if(regHost==null){
            throw new NullPointerException("regHost ==null");
        }
        if(regPort<0){
            throw new IllegalArgumentException("regPort<0");
        }
        this.regHost=regHost;
        this.regPort=regPort;
    }

}
