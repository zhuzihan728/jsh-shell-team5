package uk.ac.ucl.jsh.applications;

import uk.ac.ucl.jsh.applications.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.String;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class Pwd implements Application {
    public Pwd(){
        
    }
    public void exec(ArrayList<String> args, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        writer.write(getCurrentDirectory());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }
}
