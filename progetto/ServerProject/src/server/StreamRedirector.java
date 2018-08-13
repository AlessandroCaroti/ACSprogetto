package server;

import java.io.*;

public class StreamRedirector {

    final private static InputStream stdIn  = System.in;
    final private static PrintStream stdOut = System.out;
    final private static PrintStream stdErr = System.err;


    //Ritorna uno stream sul quale la roba che si scrive viene ricevuta dallo standardInput
    static public OutputStream redirectStdIn() throws IOException {
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream input = new PipedInputStream(output);
        System.setIn(input);
        return output;
    }

    //Ritorna uno stream nel quale si riceve le cose stampate nello standardOutput
    static public InputStream redirectStdOut() throws IOException {
        PipedInputStream input = new PipedInputStream();
        PipedOutputStream output = new PipedOutputStream(input);
        System.setOut(new PrintStream(output, true));
        return input;
    }

    //Ritorna uno stream nel quale si riceve le cose stampate nello standardError
    static public InputStream redirectStdErr() throws IOException {
        PipedInputStream input = new PipedInputStream();
        PipedOutputStream output = new PipedOutputStream(input);
        System.setErr(new PrintStream(output, true));
        return input;
    }


    //Resetta lo standard input a quello originale
    static public void resetStdIn() throws IOException {
        System.setIn(stdIn);
    }

    //Resetta lo standard input a quello originale
    static public void resetStdOut() throws IOException {
        System.setOut(stdOut);
    }

    //Resetta lo standard input a quello originale
    static public void resetStdErr() throws IOException {
        System.setOut(stdErr);
    }

    static public void resetAllStdStreams() throws IOException {
        resetStdIn();
        resetStdOut();
        resetStdErr();

    }


}
