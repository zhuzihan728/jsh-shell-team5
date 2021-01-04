package uk.ac.ucl.jsh.command;

import uk.ac.ucl.jsh.parser.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Concrete class that defines the structure of the pipe type Command.
 */
public class Pipe implements Command {
    /**
     * The command on the left hand side of the pipe operator.
     */
    private final Command left;
    /**
     * The command on the right hand side of the pipe operator.
     */
    private final Command right;
    
    /**
     * Construct a new instance of a pipe node.
     * @param left Left hand side of the pipe operator.
     * @param right Right hand side of the pipe operator.
     */
    public Pipe(Command left, Command right){
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the command on the left hand side of the command
     * to other part of the code.
     * 
     * @return Left Command
     */
    public Command getLeft(){
        return left;
    }

    /**
     * Returns the command on the right hand side of the command
     * to other part of the code.
     * 
     * @return Right Command
     */
    public Command getRight(){
        return right;
    }

    /**
     * Implementation of the evaluation function used in the visitor pattern.
     */
    @Override
    public void eval(JshCaller caller, InputStream input, OutputStream output) throws JshException {
        caller.call(this,input,output);
    }

    @Override
    public String getString() {
        return left.getString() + " PIPE " + right.getString();
    }
}
