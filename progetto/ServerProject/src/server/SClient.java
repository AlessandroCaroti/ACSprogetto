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
import utility.ResponseCode;
import utility.ServerInfo;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import static java.util.Objects.requireNonNull;


public class SClient implements Callable<Integer> {
    private List<AnonymousClientExtended> clients;
    private List<ServerInfo> serverList;


    private String myPublicKey;
    private String myPrivateKey;
    private boolean pedantic=true;
    private Server myServer;

    public SClient(String myPublicKey, String myPrivateKey,List serverList,Server myServer) throws RemoteException {
        if(myPublicKey==null||myPrivateKey==null||serverList==null )
        {
            throw new NullPointerException("passing null argument to SClient constructor");
        }
        this.myPublicKey=myPublicKey;
        this.myPrivateKey=myPrivateKey;
        this.serverList=serverList;
        this.myServer=requireNonNull(myServer);
    }





    public Integer call()
    {
        //INIT

        try {
            pedanticInfo("initializing connection with brokers");
            this.initAndConnectAnonymousClients(serverList.size());
            pedanticInfo("initializing accounts.");
            for (AnonymousClientExtended it:clients) {
                this.registerOnServer(it);
            }
            pedanticInfo("subscribing for notifications.");
            for (AnonymousClientExtended it:clients) {
                this.subscribeForNotifications(it);
            }
            pedanticInfo("subscribing to all topics.");
            for (AnonymousClientExtended it:clients) {
                this.subscribeToAllTopics(it);
            }



        } catch (RemoteException e) {
            errorStamp(e,"unable to create anonymous clients.");
            return 1;
        }


        return 0;
    }


    //PRIVATE METHODS
    private void initAndConnectAnonymousClients(int initialSize){
        this.clients=new ArrayList<>(initialSize);
        Iterator it=serverList.iterator();
        int oldSize=serverList.size();
        int i=0;

        while(it.hasNext()){

                it.next();
                clients.add(new AnonymousClientExtended(this.myPublicKey,this.myPrivateKey,this.myServer));
                clients.get(i).setServerInfo(((ServerInfo)it).regHost,((ServerInfo)it).regPort,);
                i++;

        }


        infoStamp("connected to "+i+"/"+oldSize+" servers.");
    }

    /*
    private void connectToServerList(){//todo handle in a better way creation of anontmous client and connection
        Iterator iterator=serverList.iterator();
        int oldSize=serverList.size();
        int i=0;
        while(iterator.hasNext()){
            try {
                iterator.next();
                clients.get(i).setServerInfo(((ServerInfo)iterator).regHost,((ServerInfo)iterator).regPort,"Server-"+((ServerInfo)iterator).regHost+":"+((ServerInfo)iterator).regPort);
                i++;
            }catch(Exception e){
                errorStamp(e,"Unable to connect with server:   "+((ServerInfo)iterator).regHost+":"+((ServerInfo)iterator).regPort);
                iterator.remove();//dato che non mi sono connesso lo rimuovo dalla lista.
            }
        }
        infoStamp("connected to "+i+"/"+oldSize+" servers.");

    }*/

    private void registerOnServer(AnonymousClientExtended client){
        if(client==null)throw new NullPointerException("anonymousclientextended==null");
        boolean result=client.register();
            if(result) {
                pedanticInfo("registration successfully completed.");
            }else{
                pedanticInfo("unable to register on the server.");
            }
        }


    private void subscribeForNotifications(AnonymousClientExtended client){
        if(client==null)throw new NullPointerException("anonymousclientextended==null");
        try {
            ResponseCode resp=client.getServer_stub().subscribeNewTopicNotification(client.getCookie());
            if(resp.IsOK()){
                pedanticInfo("successfully subscribed to notification list.");
            }else{
                pedanticInfo("unable to register for notification list.");
            }
        }catch(Exception exc){
            errorStamp(exc);
        }
    }

    private void subscribeToAllTopics(AnonymousClientExtended client){
        if(client==null)throw new NullPointerException("anonymousclientextended==null");
        boolean result;
        String[]topics;
        topics = client.getTopics();
        for (String topic : topics) {
            result = client.subscribe(topic);
            if(!result){
                pedanticInfo("unable to subscribe to "+topic+"on the server.");
            }else {
                myServer.addTopic(topic);
            }
        }
    }
    //METODI UTILIZZATI PER LA GESTIONE DELL'OUTPUT DEL SCLIENT

    private void errorStamp(Exception e){
        System.out.flush();
        System.err.println("[SCLIENT-ERROR]");
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void errorStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[SCLIENT-ERROR]: "      + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        e.printStackTrace();
    }

    private void warningStamp(Exception e, String msg){
        System.out.flush();
        System.err.println("[SCLIENT-WARNING]: "    + msg);
        System.err.println("\tException type: "    + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    private void infoStamp(String msg){
        System.out.println("[SERVER-INFO]: " + msg);
    }

    private void pedanticInfo(String msg){
        if(pedantic){
            infoStamp(msg);
        }
    }

}
