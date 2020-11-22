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


public class Grep implements Application{
    private String pattern;
    private Boolean read_input = false;
    private void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.isEmpty()) {
            throw new JshException("grep: missing argument");
        }
        else if (appArgs.size() == 1 ){
            if (input == null){
                throw new JshException("grep: missing InputStream");
            }
            read_input = true;
        }
        pattern = appArgs.get(0);
    }

    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
	    OutputStreamWriter writer = new OutputStreamWriter(output);
        if(read_input){
            String line;
            Scanner in = new Scanner(input);
            while(in.hasNext()) {
                line = in.nextLine();
                if (PatternMatcher.findPattern(line,pattern)){
                    try {
                        writer.write(line);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    } catch (IOException e) {
                        throw new JshException("grep: "+e.getMessage());
                    }                  
                }
            }
        }
        else{
            String fileName;
            boolean moreFiles = appArgs.size() > 2;
            for (int i = 1; i<appArgs.size(); i++){
                fileName = appArgs.get(i);
                try{
                    for (String a:InputReader.fileContent_List(fileName)){
                        if (PatternMatcher.findPattern(a, pattern)){
                            if(moreFiles){
                                writer.write(fileName);
                                writer.write(":");
                            }
                            writer.write(a);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                }
                catch(IOException e){ throw new JshException("grep: " + e.getMessage()); } 
            }
        }
    }
}
