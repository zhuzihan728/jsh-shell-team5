package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

/**
 * The pwd application implements Application, it outputs the current working
 * directory followed by a newline
 */
public class Pwd implements Application {

    /**
     * a method that executes the application pwd
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
            writer.write(WorkingDr.getInstance().getWD());
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        } catch (IOException e) {
            throw new JshException("pwd: " + e.getMessage());
        }

    }

}
