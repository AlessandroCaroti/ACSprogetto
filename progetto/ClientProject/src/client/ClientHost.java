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
package client;

import utility.gui.GuiInterface;
import java.util.concurrent.*;
import Events.*;

public class ClientHost {
    private ExecutorService userInterfaceThread;
    private ExecutorService clientThread;


    private GuiInterface userInterface;
    private ClientEngine client;

    private ConcurrentLinkedQueue<Event> clientEngineToGUI=new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Event> guiToClienEngine= new ConcurrentLinkedQueue<>();

    private ClientHost(boolean usingUserInterface) {
        userInterface = new GuiInterface(usingUserInterface);
        userInterfaceThread = Executors.newSingleThreadExecutor();
        clientThread = Executors.newSingleThreadExecutor();
    }


    public static void main(String[] args) {

        Future<Integer> exitCodeClient, exitCodeUserInterface;
        int exitcode;
        if (args.length < 1) {
            System.err.println("args: userinterface(true/false) ");
            return;
        }

        //INIT AND START
        try {

            ClientHost host = new ClientHost(Boolean.parseBoolean(args[0]));


            exitCodeClient = host.clientThread.submit(host.client);
            exitCodeUserInterface = host.userInterfaceThread.submit(host.userInterface);


            while (true) {

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
                            host.clientThread.awaitTermination(10, TimeUnit.SECONDS);
                            return;
                        case 1://errore restarting...
                            host.userInterfaceThread.submit(host.userInterface);
                            break;
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace(System.err);
                } catch (TimeoutException e) {/*tutto normale*/}

                /*
                 * CLIENT/ANONYMOUSCLIENT
                 *
                 *
                 *
                 */
                try {
                    exitcode = exitCodeClient.get(100, TimeUnit.MILLISECONDS);
                    switch (exitcode) {
                        case 0://chiudo tutto
                            host.userInterfaceThread.awaitTermination(10, TimeUnit.SECONDS);
                            return;
                        case 1://errore restarting...
                            host.clientThread.submit(host.client);
                            break;
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace(System.err);
                } catch (TimeoutException e) {/*tutto normale*/}

            }

        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        }
    }
}
