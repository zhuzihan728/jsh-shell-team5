package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.JshCaller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pipe implements Command {
    private Command left;
    private Command right;
    public Pipe(Command left, Command right){
        this.left = left;
        this.right = right;
    }

    public Command getLeft(){
        return left;
    }

    public Command getRight(){
        return right;
    }

    @Override
    public void eval(JshCaller caller, InputStream input, OutputStream output) throws IOException {
        caller.call(this,input,output);
    }
}
