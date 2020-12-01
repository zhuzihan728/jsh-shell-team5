package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * The tail application extends head application, it Prints the last N lines of
 * the file (or input stream). If there are less than N lines, print the
 * existing lines without raising an exception.
 */
public class Tail extends Head {

    /**
     * a method that takes lines of file/input stream and writes the last certain
     * number of lines to the output stream
     * 
     * @param lines     All lines of file/input stream as an arrayList
     * @param headLines The number of lines to be written
     * @param writer    The stream writer to write output
     * @throws IOException Exception thrown if the writer cannot write to the output
     *                     stream
     */
    @Override
    public void writeToShell(ArrayList<String> lines, int headLines, OutputStreamWriter writer) throws IOException {
        int limit = headLines;
        if (headLines > lines.size()) {
            limit = lines.size();
        }
        for (int i = limit; i >= 1; i--) {
            writer.write(lines.get(lines.size() - i));
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

    /**
     * a method to get the name of the application
     * 
     * @return String returns the name of the application
     */
    @Override
    public String getName() {
        return "tail";
    }
}