package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.parser.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The Interface of type command that encapsulates the raw command.
 */
public interface Command {

    /**
     * Function that evaluates the content according to the type of the content.
     * @param cmdline The instance of the acceptor class.
     * @param input Input stream that gets file input or standard input that could be used by the command.
     * @param output Output stream that the function writes to.
     * @throws JshException Exception could be thrown if there's any exception thrown by java at runtime.
     */
    void eval(JshCaller cmdline, InputStream input, OutputStream output) throws JshException;
}
