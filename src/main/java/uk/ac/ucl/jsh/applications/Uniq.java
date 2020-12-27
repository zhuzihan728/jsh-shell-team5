package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;

/**
 * The uniq application implements Application, it helps to detect the adjacent
 * duplicate lines and also deletes the duplicate lines from an input file/stdin
 * and prints the result to stdout
 */
public class Uniq implements Application {
    /**
     * the reference to the name of file to be examined
     */
    private String uniqArg;
    /**
     * the reference to the boolean value which holds true if ignoreCase is
     * required, false otherwise
     */
    private Boolean ignoreCase;

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
        if (appArgs.size() > 2) {
            throw new JshException("uniq: too many arguments");
        }
        if (appArgs.isEmpty() && input == null) {
            throw new JshException("uniq: missing InputStream");
        } else if (appArgs.size() == 1) {
            if (appArgs.get(0).equals("-i")) {
                ignoreCase = true;
            } else {
                uniqArg = appArgs.get(0);
            }
        } else if (appArgs.size() == 2) {
            if (appArgs.get(0).equals("-i")) {
                ignoreCase = true;
                uniqArg = appArgs.get(1);
            } else {
                throw new RuntimeException("uniq: wrong argument" + appArgs.get(0));
            }
        }
    }

    /**
     * a method that executes the application uniq
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
        uniqArg = null;
        ignoreCase = false;
        checkArguments(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try {
            if (uniqArg != null) {
                writeToShell(InputReader.fileContent_List(uniqArg), writer, ignoreCase);
            } else {
                writeToShell(InputReader.input_List(new Scanner(input)), writer, ignoreCase);
            }
        } catch (IOException e) {
            throw new JshException("uniq: " + e.getMessage());
        }
    }

    /**
     * a method that takes lines of file/input, makes them uniq and writes them to
     * the output stream
     * 
     * @param listOfLines All lines of file/input as an arrayList
     * @param writer      The stream writer to write output
     * @param i           The boolean value which holds true if ignoreCase is
     *                    required, false otherwise
     * @throws IOException Exception thrown if the writer cannot write to the output
     *                     stream
     */
    static void writeToShell(ArrayList<String> ListOfLines, OutputStreamWriter writer, boolean i) throws IOException {
        String last = null;
        if (i) {
            for (String a : ListOfLines) {
                if (a.equalsIgnoreCase(last)) {
                    continue;
                }
                writer.write(a);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                last = a;
            }

        } else {
            for (String a : ListOfLines) {
                if (a.equals(last)) {
                    continue;
                }
                writer.write(a);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                last = a;
            }
        }

    }

}