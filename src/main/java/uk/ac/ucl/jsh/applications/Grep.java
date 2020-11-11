package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.PatternMatcher;


public class Grep implements Application{

    
	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (appArgs.size() < 1) {
            throw new RuntimeException("grep: wrong number of arguments");
        }
        String pattern = appArgs.get(0);
        if(appArgs.size()==1){
            String line;
            Scanner in = new Scanner(System.in);
            while(in.hasNext()) {
                line = in.nextLine();
                if (PatternMatcher.findPattern(line,pattern)){
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                }
            }

        }
        else{
            String fileName;
            boolean moreFiles = appArgs.size() > 2;
            ArrayList<String> lines = new ArrayList<>();
            for (int i = 1; i<appArgs.size(); i++){
                fileName = appArgs.get(i);
                try{
                    for (String a:InputReader.read_file(fileName)){
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
                catch(IOException e){ throw new RuntimeException("grep: cannot open " + fileName); }
                catch(RuntimeException e){ throw new RuntimeException("grep: " + fileName + " does not exist"); }
  
            }
        }
    }
}
