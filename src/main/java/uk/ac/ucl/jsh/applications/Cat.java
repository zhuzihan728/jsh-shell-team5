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
 * The cat application implements Application, it concatenates the content of
 * given files and prints on the standard output
 */
public class Cat implements Application {

    /**
     * The method which checks whether the arguments for the application is legal
     * and the input stream is not null if it is used for reading input, either of
     * these being violated leads to an exception thrown
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    private void checkArguments(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.isEmpty() && input == null) {
            throw new JshException("cat: missing InputStream");
        }
    }

    /**
     * The method that executes the application cat
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input if no file is
     *                provided from the arguments
     * @param output  The stream where the application writes the output
     * @throws JshException The custom exception that Jsh shell throws if an error
     *                      occurs
     */
    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguments(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);

        if (appArgs.isEmpty()) {
            try {
                writeToShell(new Scanner(input), writer);
            } catch (IOException e) {
                throw new JshException("cat: " + e.getMessage());
            }
        } else {
            for (String arg : appArgs) {
                try {
                    writeToShell(InputReader.file_reader(arg), writer);
                } catch (IOException e) {
                    throw new JshException("cat: " + e.getMessage());
                }
            }
        }
    }

    /**
     * The method that takes each line from the scanner and writes to the output
     * stream
     * 
     * @param in     The scanner of the file to be concatenated
     * @param writer The stream writer to write output
     * @throws IOException Exception thrown if the writer cannot write to the output
     *                     stream
     */
    private void writeToShell(Scanner in, OutputStreamWriter writer) throws IOException {
        String line;
        while (in.hasNextLine()) {
            line = in.nextLine();
            writer.write(String.valueOf(line));
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
}