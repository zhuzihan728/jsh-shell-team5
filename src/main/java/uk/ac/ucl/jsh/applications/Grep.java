package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.PatternMatcher;

/**
 * The grep application implements Application, it searches for lines containing
 * a match to the specified pattern. The output of the command is the list of
 * lines. Each line is printed followed by a newline
 */
public class Grep implements Application {
    /**
     * the reference to the pattern to be matched as a string
     */
    private String pattern;
    /**
     * the reference to the boolean value which stores true if file to be grepped is
     * not provided and input should be read, false otherwise
     */
    private Boolean read_input;

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
        if (appArgs.isEmpty()) {
            throw new JshException("grep: missing argument");
        } else if (appArgs.size() == 1) {
            if (input == null) {
                throw new JshException("grep: missing InputStream");
            }
            read_input = true;
        }
        pattern = appArgs.get(0);
    }

    /**
     * a method that executes the application grep
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
        read_input = false;
        checkArguments(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (read_input) {
            String line;
            try (Scanner in = new Scanner(input)) {
                while (in.hasNextLine()) {
                    line = in.nextLine();
                    if (PatternMatcher.findPattern(line, pattern)) {
                        try {
                            writer.write(line);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        } catch (IOException e) {
                            throw new JshException("grep: " + e.getMessage());
                        }
                    }
                }
            }
        } else {
            String fileName;
            boolean moreFiles = appArgs.size() > 2;
            for (int i = 1; i < appArgs.size(); i++) {
                fileName = appArgs.get(i);
                try {
                    for (String a : InputReader.fileContent_List(fileName)) {
                        if (PatternMatcher.findPattern(a, pattern)) {
                            if (moreFiles) {
                                writer.write(fileName);
                                writer.write(":");
                            }
                            writer.write(a);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                } catch (IOException e) {
                    throw new JshException("grep: " + e.getMessage());
                }
            }
        }
    }
}
