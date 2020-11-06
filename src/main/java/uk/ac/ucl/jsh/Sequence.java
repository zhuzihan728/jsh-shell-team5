package uk.ac.ucl.jsh;

import java.io.OutputStream;

public class Sequence implements Command{
    private Command first;
    private Command second;
    public Sequence(Command first, Command second){
        this.first = first;
        this.second = second;
    }
    @Override
    public void eval(Command cmdline, OutputStream output) {
        cmdline.eval(first,output);
        cmdline.eval(second,output);
    }
}
