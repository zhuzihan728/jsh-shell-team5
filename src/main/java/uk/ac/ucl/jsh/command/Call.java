package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.JshCaller;
import uk.ac.ucl.jsh.applications.Application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Call implements Command {
    private String call;

    public Call(String call){
        this.call = call;
    }

    @Override
    public void eval(JshCaller cmdline, InputStream input, OutputStream output) throws IOException {
        cmdline.call(this,input,output);
    }

    public String getString(){
        return call;
    }
}
