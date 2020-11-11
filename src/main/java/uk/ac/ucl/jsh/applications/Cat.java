package uk.ac.ucl.jsh.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.InputReader;




public class Cat implements Application{

    
	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (appArgs.isEmpty()) {
            throw new RuntimeException("cat: missing arguments");
        } 
        else {
            for (String arg : appArgs) {
                try (BufferedReader reader = InputReader.file_reader(arg)) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        writer.write(String.valueOf(line));
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                } 
                catch (IOException e) {throw new RuntimeException("cat: cannot open " + arg);}
                catch (RuntimeException e) {throw new RuntimeException("cat: file does not exist");}
            }
        }
    }
}