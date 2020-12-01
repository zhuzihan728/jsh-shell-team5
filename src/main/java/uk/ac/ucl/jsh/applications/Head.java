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
 * The head application implements Application, it prints first N lines of the
 * file (or input stream). If there are less than N lines, print only the
 * existing lines without raising an exception
 */
public class Head implements Application {
    /**
     * the reference to the number of lines required
     */
    public Integer headLines = 10;
    /**
     * the reference to the file to be headed
     */
    public String headArg = null;

    /**
     * a method which checks whether the arguments for the application is legal and
     * the input stream is not null if it is used for reading input
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    public void checkArguments(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size() > 3) {
            throw new JshException(getName() + ": too many arguments");
        }
        if (appArgs.size() == 2 || appArgs.size() == 3) {
            if (!appArgs.get(0).equals("-n")) {
                throw new JshException(getName() + ": wrong argument " + appArgs.get(0));
            }
            try {
                headLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new JshException(getName() + ": wrong argument " + appArgs.get(1));
            }
        }
        if ((appArgs.isEmpty() || appArgs.size() == 2) && input == null) {
            throw new JshException(getName() + ": missing InputStream");
        } else if (appArgs.size() == 1) {
            headArg = appArgs.get(0);
        } else if (appArgs.size() == 3) {
            headArg = appArgs.get(2);
        }
    }

    /**
     * a method that executes the application head
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
        try {
            if (headArg != null) {
                writeToShell(InputReader.fileContent_List(headArg), headLines, writer);
            } else {
                writeToShell(InputReader.input_List(new Scanner(input)), headLines, writer);
            }
        } catch (IOException e) {
            throw new JshException(getName() + ": " + e.getMessage());
        }
    }

    /**
     * a method that takes lines of file/input and writes the first certain number of lines to
     * the output stream
     * 
     * @param lines     All lines of file/input as an arrayList
     * @param headLines The number of lines to be written
     * @param writer    The stream writer to write output
     * @throws IOException Exception thrown if the writer cannot write to the output
     *                     stream
     */
    public void writeToShell(ArrayList<String> lines, int headLines, OutputStreamWriter writer) throws IOException {
        int limit = headLines;
        if (headLines > lines.size()) {
            limit = lines.size();
        }
        for (int i = 1; i <= limit; i++) {
            writer.write(lines.get(i - 1));
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

    /**
     * a method to get the name of the application
     * 
     * @return String returns the name of the application
     */
    public String getName() {
        return "head";
    }
}