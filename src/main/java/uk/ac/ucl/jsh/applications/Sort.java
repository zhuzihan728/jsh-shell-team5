package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;

/**
 * The sort application implements Application, it sorts the contents of a
 * file/stdin line by line and prints the result on stdout
 */
public class Sort implements Application {
    /**
     * the reference to the name of file to be sorted
     */
    private String sortArg;
    /**
     * the reference to the boolean value which stores true if reverse sort is
     * required, false otherwise
     */
    private Boolean reverse;

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
            throw new JshException("sort: too many arguments");
        }
        if (appArgs.isEmpty() && input == null) {
            throw new JshException("sort: missing InputStream");
        }
        if (appArgs.size() == 1) {
            if (appArgs.get(0).equals("-r")) {
                reverse = true;
            } else {
                sortArg = appArgs.get(0);
            }
        } else if (appArgs.size() == 2) {
            if (appArgs.get(0).equals("-r")) {
                reverse = true;
                sortArg = appArgs.get(1);
            } else {
                throw new JshException("sort: wrong arguments");
            }
        }
    }

    /**
     * a method that executes the application sort
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
        sortArg = null;
        reverse = false;
        checkArguments(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try {
            if (sortArg != null) {
                writeToShell(InputReader.fileContent_List(sortArg), writer, reverse);
            } else {
                writeToShell(InputReader.input_List(new Scanner(input)), writer, reverse);
            }
        } catch (IOException e) {
            throw new JshException("sort: " + e.getMessage());
        }
    }

    /**
     * a method that takes lines of file/input, sorts them and writes them to the
     * output stream
     * 
     * @param listOfLines All lines of file/input as an arrayList
     * @param writer      The stream writer to write output
     * @param r           The boolean value which holds true if reverse sort is
     *                    required, false otherwise
     * @throws IOException Exception thrown if the writer cannot write to the output
     *                     stream
     */
    static void writeToShell(ArrayList<String> listOfLines, OutputStreamWriter writer, boolean r) throws IOException {
        listOfLines.sort(String::compareTo);
        if (r) {
            Collections.reverse(listOfLines);
        }
        for (String a : listOfLines) {
            writer.write(a);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

}