package uk.ac.ucl.jsh.applications;

import uk.ac.ucl.jsh.applications.Application;

import java.util.ArrayList;
import java.lang.String;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Echo implements Application {
    public Echo() {

    }

    public void exec(ArrayList<String> args, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        boolean atLeastOnePrinted = false;
                for (String arg : args) {
                    writer.write(arg);
                    writer.write(" ");
                    writer.flush();
                    atLeastOnePrinted = true;
                }
                if (atLeastOnePrinted) {
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                }
    }
}