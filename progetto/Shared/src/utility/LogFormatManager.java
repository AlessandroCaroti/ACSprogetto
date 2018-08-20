package utility;


import java.util.Objects;

final public class LogFormatManager {
    private boolean pedantic;
    private final String className;

    public LogFormatManager(String className, boolean pedantic) {
        this.pedantic = pedantic;
        this.className = "[" + Objects.requireNonNull(className) + "]";
    }


    public void errorStamp(String msg) {
        System.err.println(this.className + ": " + msg);
    }

    public void errorStamp(Exception e) {
        System.err.println(this.className);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        if (pedantic)
            e.printStackTrace();
    }

    public void errorStamp(Exception e, String msg) {
        System.err.println(this.className + ": " + msg);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
        if (pedantic)
            e.printStackTrace();
    }

    public void warningStamp(Exception e, String msg) {
        System.err.println(this.className + ": " + msg);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    public void infoStamp(String msg) {
        System.out.println(this.className + ": " + msg);
    }

    public void pedanticInfo(String msg) {
        if (pedantic)
            infoStamp(msg);
    }

}
