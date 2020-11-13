package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.InputReader;

import java.io.*;
import java.util.ArrayList;

public class OutputRedirection implements Sub_Call{
    private ArrayList<String> arguments;
    public OutputStream output;

    public OutputRedirection(ArrayList<Sub_Call> argus) {
        this.arguments = new ArrayList<>();
        for(Sub_Call call: argus){
            this.arguments.add(call.getString());
        }
        getInputStream();
    }


    private void getInputStream(){
        try {
            output = new FileOutputStream(InputReader.getFile(arguments.get(0)));
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public String getType() {
        return "OutputRedirection";
    }

    @Override
    public OutputStream getOutput() {
        return output;
    }

    @Override
    public InputStream getInput() {
        return null;
    }

}
