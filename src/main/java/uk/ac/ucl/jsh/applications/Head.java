package uk.ac.ucl.jsh.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import uk.ac.ucl.jsh.toolkit.InputReader;


public class Head implements Application{

	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (appArgs.isEmpty()) {
            throw new RuntimeException("head: missing arguments");
        }
        if (appArgs.size() != 1 && appArgs.size() != 3) {
            throw new RuntimeException("head: wrong arguments");
        }
        if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
            throw new RuntimeException("head: wrong argument " + appArgs.get(0));
        }
        int headLines = 10;
        String headArg;
        if (appArgs.size() == 3) {
            try {
                headLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("head: wrong argument " + appArgs.get(1));
            }
            headArg = appArgs.get(2);
        } else {
            headArg = appArgs.get(0);
        }
        try (BufferedReader reader = InputReader.file_reader(headArg)) {
                for (int i = 0; i < headLines; i++) {
                    String line = null;
                    if ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
        } 
        catch (IOException e) {throw new RuntimeException("head: cannot open " + headArg);}
        catch(RuntimeException e){throw new RuntimeException("head: " + headArg + " does not exist");}
    }
}