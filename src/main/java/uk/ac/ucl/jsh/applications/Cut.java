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
 * The cut application implements Application, it cuts out the sections from
 * each line of files and writing the result to standard output
 */
public class Cut implements Application {
    /**
     * the reference to the name of the file to be used for cut
     */
    private String cutArg = null;
    /**
     * the reference to the cutting option, which specifies which bytes to be
     * extracted from each line
     */
    private String option;

    /**
     * a method which checks whether the arguments for the application is legal and
     * the input stream is not null if it is used for reading input
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input
     * @throws JshException The custom exception that Jsh shell throws if an error occurs
     */
    private void checkArguments(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size() < 2) {
            throw new JshException("cut: missing arguments");
        } else if (appArgs.size() == 2) {
            if (!appArgs.get(0).equals("-b")) {
                throw new JshException("cut: wrong argument " + appArgs.get(0));
            } else if (input == null) {
                throw new JshException("cut: missing InputStream");
            }
        } else if (appArgs.size() == 3) {
            if (!appArgs.get(0).equals("-b")) {
                throw new JshException("cut: wrong argument " + appArgs.get(0));
            }
            cutArg = appArgs.get(2);
        } else if (appArgs.size() > 3) {
            throw new JshException("cut: too many arguments");
        }

        String cutPat = "((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-))(,((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-)))*";
        option = appArgs.get(1);
        if (!PatternMatcher.matchPattern(option, cutPat)) {
            throw new JshException("cut: wrong argument " + option);
        }
    }

    /**
     * a method that executes the application cut
     * 
     * @param appArgs The arguments for the application
     * @param input   The stream where the application reads the input if no file
     *                provided from the arguments
     * @param output  The stream where the application writes the output
     * @throws JshException The custom exception that Jsh shell throws if an error occurs
     */
    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguments(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (cutArg == null) {
            try {
                writeToShell(new Scanner(input), writer, option);
            } catch (IOException e) {
                throw new JshException("cut: " + e.getMessage());
            }
        } else {
            try {
                writeToShell(InputReader.file_reader(cutArg), writer, option);
            } catch (IOException e) {
                throw new JshException("cut: " + e.getMessage());
            }
        }
    }

    /**
     * a method that takes the cutting option and returns an integer arrayList,
     * which contains the indexes of bytes to be extracted in order
     * 
     * @param option The cutting option
     * @return ArrayList<Integer> The arrayList containing indexes of bytes to be
     *         extracted from each line
     */
    static ArrayList<Integer> optionSplit(String option) {
        String[] optionLs = option.split(",");
        ArrayList<Integer> cutLs = new ArrayList<Integer>();
        Integer toInf = 0;
        Integer from;
        Integer to;
        ArrayList<Integer> out = new ArrayList<Integer>();
        for (String a : optionLs) {
            if (!a.contains("-")) {
                from = Integer.parseInt(a);
                if (from >= toInf && toInf != 0) {
                    continue;
                }
                cutLs.add(from);
            } else {
                if (a.startsWith("-")) {
                    to = Integer.parseInt(a.split("-")[1]);
                    if (to >= toInf && toInf != 0) {
                        return out = null;
                    }
                    for (int i = 1; i <= to; i++) {
                        cutLs.add(i);

                    }
                } else if (a.endsWith("-")) {
                    from = Integer.parseInt(a.split("-")[0]);
                    if (from >= toInf && toInf != 0) {
                        continue;
                    }
                    cutLs.add(from);
                    if (!cutLs.remove(toInf)) {
                        cutLs.add(Integer.MAX_VALUE);
                    }
                    toInf = from;
                } else {
                    String[] range = a.split("-");
                    from = Integer.parseInt(range[0]);
                    to = Integer.parseInt(range[1]);
                    for (int i = from; i <= to; i++) {
                        if (i >= toInf && toInf != 0) {
                            break;
                        }
                        cutLs.add(i);
                    }

                }

            }
        }
        cutLs.sort(Integer::compareTo);
        for (Integer a : cutLs) {
            if (out.contains(a)) {
                continue;
            }
            out.add(a);

        }
        return out;
    }

    /**
     * a method that takes each line from the scanner, extracts desired bytes, and
     * writes them to the output stream
     * 
     * @param in     The scanner of the file to be cut
     * @param writer The stream writer to write output
     * @param option The cutting option
     * @throws IOException Exception thrown if the writer cannot write to the output
     *                     stream
     */
    static void writeToShell(Scanner in, OutputStreamWriter writer, String option) throws IOException {
        ArrayList<Integer> cutNum = optionSplit(option);
        Integer toInf = 0;
        if (cutNum.contains(Integer.MAX_VALUE)) {
            toInf = cutNum.get(cutNum.size() - 2);
        }
        String input;
        String output;
        while (in.hasNext()) {
            output = "";
            input = in.nextLine();
            for (Integer i : cutNum) {
                if (i > input.length()) {
                    break;
                }
                if (i != toInf) {
                    output = output + String.valueOf(input.charAt(i - 1));
                } else {
                    output = output + input.substring(i - 1);
                    break;
                }

            }
            writer.write(output);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }

    }

}