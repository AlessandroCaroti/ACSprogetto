package utility;

import java.util.Objects;


final public class LogFormatManager {
    private final boolean pedantic;
    private final String className;

    public LogFormatManager(String className, boolean pedantic) {
        this.pedantic = pedantic;
        this.className = "[" + Objects.requireNonNull(className);
    }


    public void error(String msg) {
        System.err.println(this.className +"-ERROR]: " + msg);
    }

    public void error(Exception e) {
        System.err.println(this.className+"-ERROR]");
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        if (pedantic)
            e.printStackTrace();
    }

    public void error(Exception e, String msg) {
        System.err.println(this.className +"-ERROR]: " + msg);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        if (pedantic)
            e.printStackTrace();
    }

    public void warning(Exception e, String msg) {
        System.err.println(this.className + "-WARNING]: " + msg);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    public void info(String msg) {
        System.out.println(this.className +"-INFO]: " + msg);
    }

    public void pedanticInfo(String msg) {
        if (pedantic)
            info(msg);
    }

}
