package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;


public class Head implements Application{

	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        if (appArgs.size() > 3) {
            throw new RuntimeException("head: wrong arguments");
        }
        OutputStreamWriter writer = new OutputStreamWriter(output);
        int headLines = 10;
        String headArg;
        if (appArgs.size() == 2 || appArgs.size() == 3 ) {
            if(!appArgs.get(0).equals("-n")){
                throw new RuntimeException("head: wrong argument " + appArgs.get(0));
            }
            try {
                headLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("head: wrong argument " + appArgs.get(1));
            }
        }
        ArrayList<String> lines = new ArrayList<>();
        if (appArgs.isEmpty()||appArgs.size() == 2 ) {
            lines = InputReader.input_List(new Scanner(input));
        }
        else {
            if(appArgs.size() == 1){
                headArg = appArgs.get(0);
            }
            else{
                headArg = appArgs.get(2);
            }
            try {lines = InputReader.fileContent_List(headArg);} 
            catch (IOException e) {throw new RuntimeException("head: cannot open " + headArg);}
            catch(RuntimeException e){throw new RuntimeException("head: " + headArg + " does not exist");}
        }

        try{
            writeToshell(lines, headLines, writer);
        }
        catch(IOException e){ throw new RuntimeException("head: " + e.getMessage()); }
        
    }

    public void writeToshell(ArrayList<String> lines, int headLines, OutputStreamWriter writer) throws IOException {
        int limit = headLines;
        if(headLines>lines.size()){
            limit = lines.size();
        }
        for(int i =1;i<=limit;i++){
            writer.write(lines.get(i-1));
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
}