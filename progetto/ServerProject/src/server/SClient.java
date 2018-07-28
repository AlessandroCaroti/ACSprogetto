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

import client.Client;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.concurrent.Callable;

public class SClient implements Callable<Integer> {
    private Client[] clients;
    private static final int DEFAULTCONNECTIONNUMBER=10;


    private String myUsername;
    private String plainPassword;
    private String myPublicKey;
    private String myPrivateKey;

    public SClient(String username, String plainPassword, String bp_key, String my_private_key)
            throws RemoteException
    {

        try {
            Properties sClientSettings = new Properties();//setting del server
            FileInputStream in = new FileInputStream("config.serverSettings");
            sClientSettings.load(in);
            in.close();
            this.clients = new Client[Integer.parseInt(sClientSettings.getProperty("maxbrokerconnection"))];
        }catch(IOException exc){
            System.err.println("ERROR:unable to open or read config.serverSettings");
            this.clients=new Client[DEFAULTCONNECTIONNUMBER];
        }

        if(username==null||plainPassword==null||bp_key==null||my_private_key==null)
        {
            throw new NullPointerException("passing null argument to SClient constructor");
        }
        this.myUsername=username;
        this.plainPassword=plainPassword;
        this.myPublicKey=bp_key;
        this.myPrivateKey=my_private_key;
    }




    public Integer call()
    {
        //INIT












        return 0;
    }


}
