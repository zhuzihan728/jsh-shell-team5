package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.parser.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;

public interface Command {
    void eval(JshCaller cmdline, InputStream input, OutputStream output) throws JshException;
}
