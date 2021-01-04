package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.parser.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Concrete class that defines the structure of the Call type Command.
 */
public class Call implements Command {

    /**
     * Content of the call command.
     */
    private final String call;

    /**
     * Constructed the call command using a single raw String.
     * @param call Content of the call command.
     */
    public Call(String call) {
        this.call = call;
    }

    /**
     * Implementation of the evaluation function used in the visitor pattern.
     */
    @Override
    public void eval(JshCaller cmdline, InputStream input, OutputStream output) throws JshException {
        cmdline.call(this,input,output);
    }

    /**
     * Getter function for getting the content of the command.
     * @return string
     */
    @Override
    public String getString(){
        return call;
    }
}
