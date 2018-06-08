package utility;

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
