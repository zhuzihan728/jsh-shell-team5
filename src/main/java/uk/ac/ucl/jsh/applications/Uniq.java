package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;


public class Uniq implements Application{
    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (appArgs.size()>2){
            throw new RuntimeException("uniq: too many arguments");
        }
        String uniqArg = null;
        Boolean ignoreCase = false;
        if (appArgs.size() == 1) {
            if(appArgs.get(0).equals("-i")){
                ignoreCase = true;
            }
            else {
                uniqArg = appArgs.get(0);
            }      
        }
        else if (appArgs.size() == 2){
            if(appArgs.get(0).equals("-i")){
                ignoreCase = true;
                uniqArg = appArgs.get(1);
            }
            else{
                throw new RuntimeException("uniq: wrong arguments");
            }
        }
        if(uniqArg != null) {
            try{
                writeToShell(InputReader.fileContent_List(uniqArg), writer, ignoreCase);
            }
            catch(IOException e){ throw new RuntimeException("sort: cannot open " + uniqArg); }
            catch(RuntimeException e){ throw new RuntimeException("sort: " + uniqArg + " does not exist"); }
            
        }
        else{
            try{
                writeToShell(InputReader.input_List(new Scanner(input)), writer, ignoreCase);
            }
            catch (IOException e){ throw new RuntimeException("sort: " + e.getMessage()); }
            
        }
        
    }

    static void writeToShell(ArrayList<String> listoflines, OutputStreamWriter writer, boolean i) throws IOException {
        String last=null;
        if (i){
            for(String a:listoflines){
                if (a.equalsIgnoreCase(last)){
                    continue;
                }
                writer.write(a);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                last = a;
            }

        }
        else{
            for(String a:listoflines){
            if (a.equals(last)){
                continue;
            }
            writer.write(a);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
            last = a;
            }
        }    

    }
}