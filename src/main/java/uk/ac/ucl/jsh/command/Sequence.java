package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.parser.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Concrete class that defines the structure of the Sequence type Command.
 */
public class Sequence implements Command {

    /**
     * Part of the command before the sequence operator.
     */
    private final Command first;

    /**
     * Part of the command after the sequence operator.
     */
    private final Command second;

    /**
     * Constructor that builds a instance of the class using 2 commands inside a sequence command.
     * @param first Part of the command before the sequence operator.
     * @param second Part of the command after the sequence operator.
     */
    public Sequence(Command first, Command second){
        this.first = first;
        this.second = second;
    }

    /**
     * Getter function for the command before the sequence operator.
     * @return Command before sequence operator.
     */
    public Command getFirst(){return first;}

    /**
     * Getter function for the command after the sequence operator.
     * @return Command aterf sequence operator.
     */
    public Command getSecond(){return second;}

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
    public String getString() {
        return first.getString() + " SEQ " +second.getString();
    }
}
