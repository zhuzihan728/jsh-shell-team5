package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;

public interface Command {
    public void eval(JshCaller cmdline, InputStream input, OutputStream output) throws JshException;
}
