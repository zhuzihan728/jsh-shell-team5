package uk.ac.ucl.jsh;

import java.io.OutputStream;

public interface Command {
    public void eval(Command cmdline, OutputStream output);
}
