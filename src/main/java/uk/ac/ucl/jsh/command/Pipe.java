package uk.ac.ucl.jsh.command;

import java.io.InputStream;
import java.io.OutputStream;

public class Pipe implements Command {
    private Command left;
    private Command right;
    public Pipe(Command left, Command right){
        this.left = left;
        this.right = right;
    }

    @Override
    public void eval(Command cmdline, InputStream inputStream, OutputStream output) {

    }
}
