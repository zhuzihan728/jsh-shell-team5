package uk.ac.ucl.jsh.applications;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.JshException;

/**
 * the interface of applications
 */
public interface Application {
    /**
     * The method to execute applications which implement this interface
     * 
     * @param args   The arguments for the application
     * @param input  The stream where the application reads the input
     * @param output The stream where the application writes the output
     * @throws JshException The custom exception that Jsh shell throws if an error occurs
     */
    void exec(ArrayList<String> args, InputStream input, OutputStream output) throws JshException;
}