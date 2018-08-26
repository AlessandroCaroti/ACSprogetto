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

import java.io.IOException;
import java.util.concurrent.*;
import Events.*;
//import guiClient.ClientGUI;


public class ClientHost {
    private static final int ERROR=1;
    private static final int EXIT=0;

    private LinkedBlockingQueue<Event> clientEngineToGUI=new LinkedBlockingQueue<>();//la coda consumata da GUI o terminalinterface e riempita da clientEngine
    private LinkedBlockingQueue<Event> guiToClientEngine= new LinkedBlockingQueue<>();//la coda consumata da clientEngine e riempita da TerminalInterface oppure GUI
    private  boolean guiActivated;//se è attiva l'interfaccia grafica (Se è attiva la GUI non è attiva terminalInterface e viceversa



    //Engine
    private ExecutorService clientThread;//il thread che esegue il clientEngine
    private ClientEngine clientEngine;//la classe che si occupa della gestione degli eventi(pkg. Events) dell'appliczione lato client.

    //se viene utilizzato il terminale
    private TerminalInterface terminalInterface;// la classe che gestisce l'insermento da terminale
    private ExecutorService terminalInterfaceThread;//il thread che esegue l'interfaccia da terminale

    //interfaccia grafica
    //private ClientGUI userInterface;




    private ClientHost(boolean usingUserInterface) throws IOException {
        this.guiActivated=usingUserInterface;
        clientEngine=new ClientEngine(clientEngineToGUI,guiToClientEngine);
        clientThread = Executors.newSingleThreadExecutor();

        if(guiActivated) {
            //userInterface = new ClientGUI(clientEngineToGUI, guiToClientEngine);

        }else{
            terminalInterface=new TerminalInterface(clientEngineToGUI,guiToClientEngine);
            terminalInterfaceThread = Executors.newSingleThreadExecutor();
        }

    }

    /** Il main si articola in uno step iniziale e un loop composto da 2 steps:
     *
     *  init step:
     *      viene deciso in base ad args se creare GUI o TerminalInterface
     *
     *  loop:
     *
     *      1)Tramite il future exitCodeTerminalInterface viene controllato se il thread dell'interfaccia grafica è:
     *          -uscito senza errori(0)-> chiudo l'applicazione
     *          -uscito con error(1) -> la GUI o TerminalInterface viengono  riavviati
     *          -non è ancora uscito-> continuo con il mio loop
     *
     *      2)Tramite il future exitCodeClient viene controllato se il thread clientEngine è:
     *          -uscito senza errori(0)-> chiudo l'applicazione
     *          -uscito con error(1) -> clientEngine viene  riavviato
     *          -non è ancora uscito-> continuo con il mio loop
     *
     * NOTA:
     *  Quando vengono creati clientEngine e l'interfaccia (TerminalInterface oppure GUI) vengono passati ai costruttori
     *  le due code clientEngineToGUI guiToClientEngine.
     *  Spiegazioni ulteriori sul funzionamento di queste due code in ClientEngine e TerminalInterface
     *  
     * @param args true se si vuole utilizzare l'interfaccia grafica, false se si vuole usare quella da terminale.
     */

    public static void main(String[] args){

        Future<Integer> exitCodeClient, exitCodeTerminalInterface=null;
        if (args.length < 1) {
            System.err.println("args: userinterface(true/false) ");
            return;
        }

        //INIT AND START
        try {

            ClientHost host = new ClientHost(Boolean.parseBoolean(args[0]));
            exitCodeClient = host.clientThread.submit(host.clientEngine);
            if(host.guiActivated) {
                //EventQueue.invokeLater(host.userInterface);
            }else{
                exitCodeTerminalInterface=host.terminalInterfaceThread.submit(host.terminalInterface);
            }


            while (true) {

                if(host.guiActivated) {
                    /**
                     * GRAPHICAL INTERFACE
                     *
                     *
                     *
                     **/
                    /*try {

                        switch (exitCode) {
                            case EXIT://chiudo tutto
                                host.clientThread.awaitTermination(10, TimeUnit.SECONDS);
                                return;
                            case ERROR://errore restarting...
                                EventQueue.invokeLater((host.userInterface = new ClientGUI(host.clientEngineToGUI, host.guiToClientEngine)));
                                break;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace(System.err);
                    } catch (TimeoutException e) {/*tutto normale**/

                }else {

                    /**
                     * TERMINAL INTERFACE
                     *
                     *
                     **/
                    try {
                        switch ( exitCodeTerminalInterface.get(100, TimeUnit.MILLISECONDS)) {
                            case EXIT://chiudo tutto
                                host.clientThread.awaitTermination(10, TimeUnit.SECONDS);
                                System.exit(0);
                                break;
                            case ERROR://errore restarting...
                                exitCodeTerminalInterface=host.terminalInterfaceThread.submit(host.terminalInterface= new TerminalInterface(host.clientEngineToGUI,host.guiToClientEngine));
                                break;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace(System.err);
                    } catch (TimeoutException e) {/*tutto normale*/}
                }

                /**
                 * CLIENT/ANONYMOUSCLIENT
                 *
                 *
                 *
                 **/
                try {
                    switch (exitCodeClient.get(100, TimeUnit.MILLISECONDS)) {
                        case EXIT://chiudo tutto
                            //host.userInterfaceThread.awaitTermination(10, TimeUnit.SECONDS); todo check
                            System.exit(0);
                            break;
                        case ERROR://errore restarting...
                            host.clientThread.submit((host.clientEngine=new ClientEngine(host.clientEngineToGUI,host.guiToClientEngine)));
                            break;
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace(System.err);
                } catch (TimeoutException e) {/*tutto normale.Questa exc viene lanciata se il thread sta ancora lavorando*/}

            }

        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        }
    }
}
