package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.File;

/**
 * The cd application implements Application, it changes the current working
 * directory
 */
public class Cd implements Application {

    /**
     * The method which checks whether the arguments for the application is legal, if not, an exception is thrown
     * 
     * @param appArgs The arguments for the application
     * @throws JshException The custom exception that Jsh shell throws if an error occurs
     */
    private void checkArguments(ArrayList<String> appArgs) throws JshException {
        if (appArgs.isEmpty()) {
            throw new JshException("cd: missing argument");
        } else if (appArgs.size() > 1) {
            throw new JshException("cd: too many arguments");
        }
    }
    
    /**
     * The method that executes the application cd
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input, not used for cd
     * @param output  The stream where the application writes the output
     * @throws JshException The custom exception that Jsh shell throws if an error occurs
     */
    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguments(appArgs);
        String dirString = appArgs.get(0);
        File dir = new File(WorkingDr.getInstance().getWD(), dirString);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new JshException("cd: " + dirString + " is not an existing directory");
        }
        try {
            WorkingDr.getInstance().setWD(dir.getCanonicalPath());
        } catch (IOException e) {
            throw new JshException("cd: " + e.getMessage());
        }

    }

}