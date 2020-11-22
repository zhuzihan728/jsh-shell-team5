package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;


public class Uniq implements Application{
    private String uniqArg = null;
    private Boolean ignoreCase = false;

    private void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size()>2){
            throw new JshException("uniq: too many arguments");
        }
        if (appArgs.isEmpty()&&input==null){
            throw new JshException("uniq: missing InputStream");
        }
        else if (appArgs.size() == 1) {
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
                throw new RuntimeException("uniq: wrong argument" + appArgs.get(0));
            }
        }
    }
    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try{
            if(uniqArg != null) {   
                writeToShell(InputReader.fileContent_List(uniqArg), writer, ignoreCase); 
            }
            else{
                writeToShell(InputReader.input_List(new Scanner(input)), writer, ignoreCase);
            }
        } catch (IOException e){ throw new JshException("uniq: " + e.getMessage()); }
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