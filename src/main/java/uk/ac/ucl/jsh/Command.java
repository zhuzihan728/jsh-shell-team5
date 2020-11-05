package uk.ac.ucl.jsh;

import java.io.OutputStream;

public interface Command {
    private void eval(CommandCaller cmdline, OutputStream output);
    public void accept(CommandCaller caller);
}
