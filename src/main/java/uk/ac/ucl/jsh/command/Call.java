package uk.ac.ucl.jsh.command;

import java.io.InputStream;
import java.io.OutputStream;

public class Call implements Command {
    private String call;

    public Call(String call){
        this.call = call;
    }

    @Override
    public void eval(Command cmdline, InputStream inputStream, OutputStream output) {

    }
//    private Application application;

}
