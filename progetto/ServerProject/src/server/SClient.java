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

import client.AnonymousClient;
import utility.ServerInfo;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

public class SClient implements Callable<Integer> {
    private AnonymousClient[] clients;
    private static final int DEFAULTCONNECTIONNUMBER=10;
    private List<ServerInfo> serverList;


    private String myUsername;
    private String plainPassword;
    private String myPublicKey;
    private String myPrivateKey;
    private boolean pedantic=true;

    public SClient(String username, String plainPassword, String myPublicKey, String myPrivateKey,List serverList)
    {

        try {
            Properties sClientSettings = new Properties();//setting del server
            FileInputStream in = new FileInputStream("config.serverSettings");
            sClientSettings.load(in);
            in.close();
            this.clients = new AnonymousClient[Integer.parseInt(sClientSettings.getProperty("maxbrokerconnection"))];
        }catch(IOException exc){
            System.err.println("ERROR:unable to open or read config.serverSettings");
            this.clients=new AnonymousClient[DEFAULTCONNECTIONNUMBER];
        }

        if(username==null||plainPassword==null||myPublicKey==null||myPrivateKey==null||serverList==null)
        {
            throw new NullPointerException("passing null argument to SClient constructor");
        }
        this.myUsername=username;
        this.plainPassword=plainPassword;
        this.myPublicKey=myPublicKey;
        this.myPrivateKey=myPrivateKey;
        this.serverList=serverList;
    }


    public Integer call()
    {
        //INIT
        pedanticInfo("init connections to brokers");
        this.connectToServerList();










        return 0;
    }


    //PRIVATE METHODS

    private void connectToServerList(){
        Iterator iterator=serverList.iterator();
        int i=0;
        while(iterator.hasNext()&&i<clients.length){
            try {
                clients[i] = new AnonymousClient(this.myUsername, this.myPublicKey, this.myPrivateKey);
                clients[i].setServerInfo(((ServerInfo)iterator.next()).regHost,((ServerInfo)iterator).regPort,"Server-"+((ServerInfo)iterator).regHost+":"+((ServerInfo)iterator).regPort);

                i++;
            }catch(Exception e){
                errorStamp(e,"Unable to create client:"+i);
                iterator.remove();//dato che non mi sono connesso lo rimuovo dalla lista.
            }
        }
        infoStamp("connected to "+i+"/"+serverList.size()+" servers.");

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
