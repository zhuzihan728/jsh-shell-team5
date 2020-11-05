package uk.ac.ucl.jsh;

import java.io.OutputStream;

public interface Command {
    public void eval(CommandCaller cmdline, OutputStream output);
}
