package uk.ac.ucl.jsh.applications;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * a decorator of Application, it implements Application and decorates concrete
 * applications to be unsafe, i.e., it prints the error message on the standard
 * output, and terminates successfully without raising an exception
 */
public class Unsafe implements Application {
    /**
     * the reference to the application to be decorated unsafe
     */
    Application application;

    /**
     * constructs Unsafe application
     * 
     * @param application The application to be decorated unsafe
     */
    public Unsafe(Application application) {
        this.application = application;
    }

    /**
     * a method that executes applications, if an error occurs, it prints the error
     * message straight away instead of throwing an exception
     * 
     * @param args   The arguments for the application
     * @param input  The stream where the application reads the input
     * @param output The stream where the application writes the output
     */
    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) {
        try {
            this.application.exec(appArgs, input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}