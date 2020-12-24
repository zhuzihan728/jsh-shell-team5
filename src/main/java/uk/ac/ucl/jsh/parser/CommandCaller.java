package uk.ac.ucl.jsh.parser;

import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;
import uk.ac.ucl.jsh.toolkit.JshException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface that defines the caller class that can accept different
 * Command type dynamiclly at runtime.
 */
public interface CommandCaller {
    /**
     * Function that executes when it comes to a Command of type Pipe.
     * 
     * @param pipe the concrete type of the node visiting.
     * @param inputStream Input stream either from file or the standard input.
     * @param output Output stream that this function writes to.
     * @throws JshException Customised exception throws when there exception at runtime.
     */
    void call(Pipe pipe, InputStream inputStream, OutputStream output) throws JshException;

    /**
     * Function that executes when it comes to a Command of type Sequence.
     * 
     * @param sequence the concrete type of the node visiting.
     * @param inputStream Input stream either from file or the standard input.
     * @param output Output stream that this function writes to.
     * @throws JshException Customised exception throws when there exception at runtime.
     */
    void call(Sequence sequence,InputStream inputStream, OutputStream output) throws JshException;

    /**
     * Function that executes when it comes to a Command of type Call.
     * 
     * @param call the concrete type of the node visiting.
     * @param inputStream Input stream either from file or the standard input.
     * @param output Output stream that this function writes to.
     * @throws JshException Customised exception throws when there exception at runtime.
     */
    void call(Call call,InputStream inputStream, OutputStream output) throws JshException;
}
