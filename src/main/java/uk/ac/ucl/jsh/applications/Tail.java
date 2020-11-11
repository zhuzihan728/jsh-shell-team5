package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import uk.ac.ucl.jsh.toolkit.InputReader;



public class Tail implements Application{
    
	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (appArgs.isEmpty()) {
            throw new RuntimeException("tail: missing arguments");
        }
        if (appArgs.size() != 1 && appArgs.size() != 3) {
            throw new RuntimeException("tail: wrong arguments");
        }
        if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + appArgs.get(0));
        }
        int tailLines = 10;
        String tailArg;
        if (appArgs.size() == 3) {
            try {
                tailLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("tail: wrong argument " + appArgs.get(1));
            }
            tailArg = appArgs.get(2);
        } else {
            tailArg = appArgs.get(0);
        }
        try {
            ArrayList<String> storage = InputReader.read_file(tailArg);
            int index = 0;
            if (tailLines > storage.size()) {
                index = 0;
            } else {
                index = storage.size() - tailLines;
            }
            for (int i = index; i < storage.size(); i++) {
                writer.write(storage.get(i) + System.getProperty("line.separator"));
                writer.flush();
            }            
        } 
        catch (IOException e) {
            throw new RuntimeException("tail: cannot open " + tailArg);}
        catch (RuntimeException e){
            throw new RuntimeException("tail: " + tailArg + " does not exist");
        }
    }
}