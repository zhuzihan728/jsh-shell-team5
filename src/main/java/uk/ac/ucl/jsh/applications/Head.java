package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;


public class Head implements Application{
    public Integer headLines = 10;
    public String headArg = null;
    public void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size() > 3) {
            throw new JshException(getName() + ": too many arguments");
        }
        if (appArgs.size() == 2 || appArgs.size() == 3 ) {
            if(!appArgs.get(0).equals("-n")){
                throw new JshException(getName() + ": wrong argument " + appArgs.get(0));
            }
            try {
                headLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new JshException(getName() + ": wrong argument " + appArgs.get(1));
            }
        }
        if ((appArgs.isEmpty()||appArgs.size() == 2)&&input == null){
            throw new JshException(getName() + ": missing InputStream");
        }
        else if(appArgs.size() == 1){
            headArg = appArgs.get(0);
        }
        else if (appArgs.size() == 3){
                headArg = appArgs.get(2);
        }
    }

	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try{
            if(headArg != null) {
                writeToShell(InputReader.fileContent_List(headArg), headLines, writer);     
            }
            else{
                writeToShell(InputReader.input_List(new Scanner(input)), headLines, writer);              
            }
        } catch (IOException e){ throw new JshException(getName() + ": " + e.getMessage()); } 
    }

    public void writeToShell(ArrayList<String> lines, int headLines, OutputStreamWriter writer) throws IOException {
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

    public String getName(){
        return "head";
    }
}