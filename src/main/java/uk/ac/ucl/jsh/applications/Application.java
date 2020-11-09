package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public interface Application{
    public default String getCurrentDirectory(){
        return System.getProperty("user.dir");
    }
    public void exec(ArrayList<String> args, OutputStream output) throws IOException;
}