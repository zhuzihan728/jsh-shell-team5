package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.File;

/**
 * The ls application implements Application, it lists the content of a
 * directory
 */
public class Ls implements Application {
    /**
     * the reference to the directory whose content is to be listed
     */
    private File currDir;

    /**
     * a method which checks whether the arguments for the application is legal
     * 
     * @param appArgs The arguments for the application
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    private void checkArguments(ArrayList<String> appArgs) throws JshException {
        if (appArgs.isEmpty()) {
            currDir = new File(WorkingDr.getInstance().getWD());
        } else if (appArgs.size() == 1) {
            currDir = InputReader.getFile(appArgs.get(0));
            if (!currDir.exists()) {
                throw new JshException("ls: " + appArgs.get(0) + " does not exist");
            }
        } else {
            throw new JshException("ls: too many arguments");
        }
    }

    /**
     * a method that executes the application ls
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
        checkArguments(appArgs);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try {
            File[] listOfFiles = currDir.listFiles();
            boolean atLeastOnePrinted = false;
            for (File file : listOfFiles) {
                if (!file.getName().startsWith(".")) {
                    writer.write(file.getName());
                    writer.write("\t");
                    writer.flush();
                    atLeastOnePrinted = true;
                }
            }
            if (atLeastOnePrinted) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        } catch (IOException e) {
            throw new JshException("ls: " + e.getMessage());
        }
    }
}