package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import uk.ac.ucl.jsh.toolkit.JshException;

/**
 * The echo application implements Application, it writes its arguments
 * separated by spaces and terminates by a newline on the standard output
 */
public class Echo implements Application {

    /**
     * a method that executes the application echo
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input, not used for
     *                this application
     * @param output  The stream where the application writes the output
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try {
            if (!appArgs.isEmpty()) {
                String out = String.join(" ", appArgs);
                writer.write(out);
            }
            writer.write(System.getProperty("line.separator"));
            writer.flush();

        } catch (IOException e) {
            throw new JshException("echo: " + e.getMessage());
        }

    }
}