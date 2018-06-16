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
package utility.gui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

public class GuiInterface implements Callable<Integer> {
    /*TODO add stdin*/
    private final ByteArrayOutputStream stdErrArray = new ByteArrayOutputStream();
    private final ByteArrayOutputStream stdOutArray = new ByteArrayOutputStream();


    private final PrintStream stdErrStream;
    private final PrintStream stdOutStream;


    /**
     * @param GuiActive passare true se si vuole utilizzare l'interfaccia,false per il terminale
     */
    public GuiInterface(boolean GuiActive)
    {
        if(GuiActive){
            stdOutStream=new PrintStream(stdOutArray);
            stdErrStream=new PrintStream(stdErrArray);

            System.setOut(stdOutStream);
            System.setErr(stdErrStream);

        }
        else {
            stdErrStream=System.err;
            stdOutStream=System.out;

            System.out.println("USING TERMINAL INTERFACE");
        }
    }

    /**
     * Avvia l'interfaccia grafica
     * @return 0 quando l'utente vuole spegnere l'host,1 in caso di errore dell'interfaccia grafica
     */
    public Integer call(){



        return 0;
    }

}
