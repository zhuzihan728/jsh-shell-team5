package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;



public class Sort implements Application{
    private String sortArg = null;
    private Boolean reverse = false;

    private void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size()>2) {
            throw new JshException("sort: too many arguments");
        }
        if (appArgs.isEmpty()&&input==null){
            throw new JshException("sort: missing InputStream");
        }
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
                throw new JshException("sort: wrong arguments");
            }
        }
    }
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try{
            if(sortArg != null) {
                writeToShell(InputReader.fileContent_List(sortArg), writer, reverse);     
            }
            else{
                writeToShell(InputReader.input_List(new Scanner(input)), writer, reverse);              
            }
        } catch (IOException e){ throw new JshException("sort: " + e.getMessage()); }
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