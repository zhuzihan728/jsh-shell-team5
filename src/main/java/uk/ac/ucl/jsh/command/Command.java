package uk.ac.ucl.jsh.command;

import java.io.InputStream;
import java.io.OutputStream;

public interface Command {
    public void eval(Command cmdline, InputStream inputStream, OutputStream output);
}
