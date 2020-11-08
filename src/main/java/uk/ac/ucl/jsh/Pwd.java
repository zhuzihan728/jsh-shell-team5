package uk.ac.ucl.jsh;

import java.util.ArrayList;
import java.lang.String;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class Pwd implements Application{
    public Pwd(){
        
    }
    public void exec(ArrayList<String> args, OutputStream output){
        OutputStreamWriter writer = new OutputStreamWriter(output);
        writer.write(currentDirectory);
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }
}
