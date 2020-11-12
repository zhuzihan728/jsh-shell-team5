package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;



public class Sort implements Application{
    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (appArgs.size()>2) {
            throw new RuntimeException("sort: too many arguments");
        }

        String sortArg = null;
        Boolean reverse = false;
        if (appArgs.size() == 1) {
            if(appArgs.get(0).equals("-r")){
                reverse = true;
            }
            else {
                sortArg = appArgs.get(0);
            }                
        }      
        else if (appArgs.size() == 2){
            if(appArgs.get(0).equals("-r")){
                reverse = true;
                sortArg = appArgs.get(1);
            }
            else{
                throw new RuntimeException("sort: wrong arguments");
            }
        }
        if(sortArg != null) {
            try{
                writeToShell(InputReader.fileContent_List(sortArg), writer, reverse);
            }
            catch(IOException e){ throw new RuntimeException("sort: cannot open " + sortArg); }
            catch(RuntimeException e){ throw new RuntimeException("sort: " + sortArg + " does not exist"); }
            
        }
        else{
            try{
                writeToShell(InputReader.input_List(new Scanner(input)), writer, reverse);
            }
            catch (IOException e){ throw new RuntimeException("sort: " + e.getMessage()); }
            
        }
    }

    static void writeToShell(ArrayList<String> listoflines, OutputStreamWriter writer, boolean r) throws IOException {
        listoflines.sort(String::compareTo);
        if (r){
            Collections.reverse(listoflines);
        }
        for(String a:listoflines){
            writer.write(a);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
   
}