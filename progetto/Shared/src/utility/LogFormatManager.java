package utility;

import java.util.Objects;


final public class LogFormatManager {
    private final boolean pedantic;
    private final String className;

    public LogFormatManager(final String className, final boolean pedantic) {
        this.pedantic = pedantic;
        this.className = "[" + Objects.requireNonNull(className);
    }


    public void error(final String msg) {
        System.err.println(this.className + "-ERROR]: " + msg);
    }

    public void error(final Exception e) {
        System.err.println(this.className + "-ERROR]");
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
//        if (pedantic)
//            e.printStackTrace();
    }

    public void error(final Exception e, final String msg) {
        System.err.println(this.className + "-ERROR]: " + msg);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
//        if (pedantic)
//            e.printStackTrace();
    }

    public void error(final ResponseCode r, final String msg) {
        System.out.flush();
        System.err.println("[" + className + "-ERROR]: " + msg);
        System.err.println("\tError code: " + r.getStatusCode());
        System.err.println("\tError message: " + r.getMessageInfo());
    }

    public void warning(final String msg) {
        System.out.println(this.className + "-WARNING]: " + msg);
    }

    public void warning(final Exception e, final String msg) {
        System.err.println(this.className + "-WARNING]: " + msg);
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    public void warning(final Exception e) {
        System.err.println(this.className + "-WARNING]");
        System.err.println("\tException type: " + e.getClass().getSimpleName());
        System.err.println("\tException message: " + e.getMessage());
    }

    public void pedanticWarning(final Exception e) {
        if (pedantic)
            warning(e);
    }

    public void pedanticWarning(final Exception e, final String msg) {
        if (pedantic)
            warning(e, msg);
    }

    public void info(final String msg) {
        System.out.println(this.className + "-INFO]: " + msg);
    }

    public void pedanticInfo(final String msg) {
        if (pedantic)
            info(msg);
    }

}
