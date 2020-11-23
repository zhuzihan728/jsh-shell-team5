package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;

public class Sequence implements Command {
    private Command first;
    private Command second;
    public Sequence(Command first, Command second){
        this.first = first;
        this.second = second;
    }

    public Command getFirst(){return first;}

    public Command getSecond(){return second;}

    @Override
    public void eval(JshCaller cmdline, InputStream input, OutputStream output) throws JshException {
        cmdline.call(this,input,output);
    }
}