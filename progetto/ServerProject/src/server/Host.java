/**
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

import utility.gui.GuiInterface;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.*;

public class Host {
        private ExecutorService userInterfaceThread ;
        private ExecutorService serverThread;
        private ExecutorService sClientThread;


        private GuiInterface userInterface;
        private Server server;
        //TODO private sClient sclient;



    private Host(boolean usingUserInterface) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, AlreadyBoundException, RemoteException, UnknownHostException {
         userInterface=new GuiInterface(usingUserInterface);
         server=new Server();
         //TODO sclient=new SClient();

        userInterfaceThread=Executors.newSingleThreadExecutor();
        serverThread=Executors.newSingleThreadExecutor();
        sClientThread=Executors.newSingleThreadExecutor();
    }



    public static void  main(String[] args){

        Future<Integer> exitCodeServer,exitCodeSClient,exitCodeUserInterface;
        int exitcode;
        if(args.length<1) {
            System.err.println("args: userinterface(true/false) ");
            return;
        }

        //INIT AND START
        try {

            Host host = new Host(Boolean.parseBoolean(args[0]));

        //TODO exitCodeSClient=host.sClientThread.submit(host.sclient);
        exitCodeServer=host.serverThread.submit(host.server);
        exitCodeUserInterface=host.userInterfaceThread.submit(host.userInterface);


        while(true) {

            /*
             * USERINTERFACE
             *
             *
             *
             */
            try {
                exitcode = exitCodeUserInterface.get(100, TimeUnit.MILLISECONDS);
                switch (exitcode) {
                    case 0://chiudo tutto
                        host.sClientThread.awaitTermination(10, TimeUnit.SECONDS);
                        host.serverThread.awaitTermination(10, TimeUnit.SECONDS);
                        return;
                    case 1://errore restarting...
                        host.userInterfaceThread.submit(host.userInterface);
                        break;
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace(System.err);
            } catch (TimeoutException e) {/*tutto normale*/}



            /*
             * SERVER
             *
             *
             */
            try {
                exitcode = exitCodeServer.get(100, TimeUnit.MILLISECONDS);
                switch (exitcode) {
                    case 1://errore restarting...
                             host.serverThread.submit(host.server);
                             break;
                    case 0:/*chiudo tutto*/
                        host.sClientThread.awaitTermination(10, TimeUnit.SECONDS);
                        host.userInterfaceThread.awaitTermination(10, TimeUnit.SECONDS);
                        return;
                    }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace(System.err);
            } catch (TimeoutException e1) {/*tutto normale*/}



            /*
             * SCLIENT
             *
             */
            /*TODO
            try {
                exitcode = exitCodeSClient.get(100, TimeUnit.MILLISECONDS);
                switch (exitcode) {
                    case 1://errore restarting...
                        //TODO host.sClientThread.submit(host.sClient);
                        break;
                    case 0:
                        host.serverThread.awaitTermination(10, TimeUnit.SECONDS);
                        host.userInterfaceThread.awaitTermination(10, TimeUnit.SECONDS);
                        return;
                }
            } catch (InterruptedException | InterruptedException e) {
            e.printStackTrace(System.err);
            } catch (TimeoutException e1) {}
            */

        }

        }catch (Exception exc){
            exc.printStackTrace();
            return;
        }

    }
}
