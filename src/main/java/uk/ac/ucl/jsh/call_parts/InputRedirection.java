package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.*;
import java.util.ArrayList;

public class InputRedirection implements Sub_Call{
    private ArrayList<String> arguments;
    private InputStream input;

    public InputRedirection(ArrayList<Sub_Call> arguments){
        this.arguments = new ArrayList<>();
        for(Sub_Call call: arguments){
            this.arguments.add(call.getString());
        }
        getInputStream();
    }

    private void getInputStream(){
        try {
            input = new FileInputStream(InputReader.getFile(arguments.get(0)));
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public InputStream getInput(){
        return input;
    }

    @Override
    public ArrayList<String> get_OutputArray() throws JshException {
        return null;
    }

    public String getString(){
        return null;
    }

    @Override
    public String getType() {
        return "InputRedirection";
    }

    @Override
    public OutputStream getOutput() {
        return null;
    }

}
