package uk.ac.ucl.jsh.call_parts;

import java.io.InputStream;
import java.io.OutputStream;

public class BaseCall implements Sub_Call{
    private String call;

    public BaseCall(String call){
        this.call = call;
    }

    public String getString(){
        return call;
    }

    @Override
    public String getType() {
        return "BaseCall";
    }

    @Override
    public OutputStream getOutput() {
        return null;
    }

    @Override
    public InputStream getInput() {
        return null;
    }
}
