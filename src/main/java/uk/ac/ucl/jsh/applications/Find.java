package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;

import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.PatternMatcher;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

/**
 * The find application implements Application, it recursively searches for
 * files with matching names and outputs the list of relative paths, each
 * followed by a newline
 */
public class Find implements Application {
    /**
     * the reference to the path of the root directory to be searched
     */
    private String path = "";

    /**
     * the reference to the searching pattern
     */
    private String pattern;

    /**
     * a method which checks whether the arguments for the application is legal and
     * the input stream is not null if it is used for reading input
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    private void checkArguments(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size() < 2) {
            throw new JshException("find: missing arguments");
        } else if (appArgs.size() == 2) {
            if (!appArgs.get(0).equals("-name")) {
                throw new JshException("find: wrong argument " + appArgs.get(0));
            }
            pattern = appArgs.get(1).replace("*", ".*");
        } else if (appArgs.size() == 3) {
            if (!appArgs.get(1).equals("-name")) {
                throw new JshException("find: wrong argument " + appArgs.get(1));
            }
            path = appArgs.get(0);
            pattern = appArgs.get(2).replace("*", ".*");
        } else if (appArgs.size() > 3) {
            throw new JshException("find: too many arguments");
        }
    }

    /**
     * a method that executes the application find
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input if no file
     *                provided from the arguments
     * @param output  The stream where the application writes the output
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguments(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        File file = new File(WorkingDr.getInstance().getWD(), path);
        try {
            boolean atLeastOnePrinted = getFiles(file, pattern, writer);
            if (atLeastOnePrinted) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            } else {
                throw new JshException("find: no such file or directory");
            }
        } catch (IOException e) {
            throw new JshException("find: " + e.getMessage());
        }
    }

    /**
     * a method that recursively searches for files with matching names and outputs
     * their relative paths, each followed by a newline
     * 
     * @param file    The root directory for searching
     * @param pattern The pattern with which file name should match
     * @param writer  The stream writer to write output
     * @return boolean The method returns true if there is at least one file whose
     *         name matches with the pattern, false otherwise
     * @throws IOException Exception thrown if the writer cannot write to the output
     *                     stream
     */
    private boolean getFiles(File file, String pattern, OutputStreamWriter writer) throws IOException {
        boolean printed1 = false;
        boolean printed2 = false;
        File[] files = file.listFiles();
        for (File a : files) {
            if (a.getName().startsWith(".")) {
                continue;
            }
            if (PatternMatcher.matchPattern(a.getName(), pattern)) {
                writer.write(getRelative(a));
                writer.write("\n");
                writer.flush();
                printed1 = true;
            }
            if (a.isDirectory()) {
                if (getFiles(a, pattern, writer)) {
                    printed2 = true;
                }

            }
        }
        return (printed1 || printed2);
    }

    /**
     * a method that takes a file and returns its relative path against the root
     * directory
     * 
     * @param child The file in the root directory, or its sub-directories
     * @return String The relative path of the file against the root directory as a
     *         string
     */
    private String getRelative(File child) {
        String rp = Paths.get(WorkingDr.getInstance().getWD(), path).relativize(Paths.get(child.getAbsolutePath()))
                .toString();
        if (path.equals("")) {
            if (rp.startsWith(File.separator)) {
                return "." + rp;
            } else {
                return "." + File.separator + rp;
            }
        } else {
            if (rp.startsWith(File.separator)) {
                return path + rp;
            } else {
                return path + File.separator + rp;
            }
        }

    }

}