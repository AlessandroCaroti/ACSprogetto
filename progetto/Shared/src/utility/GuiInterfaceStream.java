package utility;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class GuiInterfaceStream {
    /*TODO add stdin*/
    private final ByteArrayOutputStream stdErrArray = new ByteArrayOutputStream();
    private final ByteArrayOutputStream stdOutArray = new ByteArrayOutputStream();


    public final PrintStream stdErrStream;
    public final PrintStream stdOutStream;


    /**
     * @param GuiActive passare true se si vuole utilizzare l'interfaccia,false per il terminale
     */
    public GuiInterfaceStream(boolean GuiActive)
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
        }
    }

}
