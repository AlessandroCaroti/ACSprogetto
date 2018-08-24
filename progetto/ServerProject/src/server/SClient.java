/*
    This file is part of ACSprogetto.

    ACSprogetto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ACSprogetto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ACSprogetto.  If not, see <http://www.gnu.org/licenses/>.

**/
package server;
import utility.LogFormatManager;
import utility.ResponseCode;
import utility.ServerInfo;
import utility.ServerInfoRecover;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static java.util.Objects.requireNonNull;


public class SClient  {
    private List<AnonymousClientExtended> clients;
    private List<ServerInfo> serverList;

    private Server myServer;
    final private LogFormatManager print;

    public SClient(List serverList,Server myServer)  {
        this(serverList, myServer, false);

    }

    public SClient(List serverList,Server myServer, boolean pedantic)  {
        if(serverList==null )
        {
            throw new NullPointerException("passing null argument to SClient constructor");
        }
        this.serverList=serverList;
        this.myServer=requireNonNull(myServer);
        this.print = new LogFormatManager("SCLIENT", pedantic);

    }





    public boolean init()
    {
        //INIT

        try {
            print.pedanticInfo("Initializing connection with brokers");
            this.initAndConnectAnonymousClients(serverList.size());
            print.pedanticInfo("Initializing accounts.");
            for (AnonymousClientExtended it:clients) {
                this.registerOnServer(it);
            }
            print.pedanticInfo("Subscribing for notifications.");
            for (AnonymousClientExtended it:clients) {
                this.subscribeForNotifications(it);
            }
            print.pedanticInfo("Subscribing to all topics.");
            for (AnonymousClientExtended it:clients) {
                this.subscribeToAllTopics(it);
            }

        } catch (Exception e) {
            print.error(e,"unable to create anonymous clients.");
            return false;
        }





        return true;
    }

    //PUBLIC METHODS

    public boolean addServer(ServerInfo serverInfo){
        try {
            AnonymousClientExtended curr;
            return (curr = addServerConnection(serverInfo)) != null
                    && registerOnServer(curr)
                    && subscribeForNotifications(curr)
                    && subscribeToAllTopics(curr);
        }catch (Exception exc){
            print.error(exc,"Error while adding server:"+serverInfo.regHost);
        }
        return false;
    }





    //PRIVATE METHODS

    /**
     *  Tenta di stabilire una connessione con tutti i server della lista serverList.
     * @param initialSize la grandezza della lista dei server
     */
    private void initAndConnectAnonymousClients(int initialSize)   {
        this.clients=new ArrayList<>(initialSize);
        Iterator it=serverList.iterator();
        int oldSize=serverList.size();
        ServerInfo curr;

        while(it.hasNext()){
                curr=(ServerInfo)it.next();
                if(addServerConnection(curr)==null||!serverList.add(curr)){
                    it.remove();
                }

        }
        print.info("Connected to "+serverList.size()+"/"+oldSize+" servers.");
    }


    private AnonymousClientExtended addServerConnection(ServerInfo serverInfo){
        try {
            ServerInfoRecover infoServer = new ServerInfoRecover();
            AnonymousClientExtended anonymousClient = new AnonymousClientExtended(this.myServer);
            String[] a = infoServer.getServerInfo(InetAddress.getByName(serverInfo.regHost));
            anonymousClient.setServerInfo(a[0], Integer.valueOf(a[1]), a[2]);
            return clients.add(anonymousClient)?anonymousClient:null;
        }catch (Exception exc){
            print.warning(exc,"Unable to add server!");
        }
        return null;
    }

    private boolean registerOnServer(AnonymousClientExtended client) throws NullPointerException {
        if(client==null)throw new NullPointerException("anonymousclientextended==null");
        boolean result=client.register();
            if(result) {
                print.pedanticInfo("registration successfully completed.");
            }else{
                print.pedanticInfo("unable to register on the server.");
            }
            return result;
        }


    private boolean subscribeForNotifications(AnonymousClientExtended client) throws NullPointerException {
        if(client==null)throw new NullPointerException("anonymousclientextended==null");
        try {
            ResponseCode resp=client.getServer_stub().subscribeNewTopicNotification(client.getCookie());
            if(resp.IsOK()){
                print.pedanticInfo("successfully subscribed to notification list.");
                return true;
            }else{
                print.pedanticInfo("unable to register for notification list.");
                return false;
            }
        }catch(Exception exc){
            print.error(exc);
        }
        return false;
    }

    private boolean subscribeToAllTopics(AnonymousClientExtended client) throws NullPointerException {
        if(client==null)throw new NullPointerException("anonymousclientextended==null");
        boolean result,exitStat=true;
        String[]topics;
        topics = client.getTopics();
        for (String topic : topics) {
            result = client.subscribe(topic);
            if(!result){
                print.pedanticInfo("unable to subscribe to "+topic+"on the server.");
                exitStat=false;
            }else {
                myServer.addTopic(topic);
            }
        }
        return exitStat;
    }

}
