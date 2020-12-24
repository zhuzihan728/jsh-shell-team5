package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Interface for parts of the sequence inside the Call command.
 */
public interface Sub_Call {

    /**
     * Getter function to get the content of the string.
     * @return Content of type string.
     */
    String getString();

    /**
     * Get the type of the command in string format.
     * @return String format of command type.
     */
    String getType();

    /**
     * Get the output stream that the sub_call writes to.
     * @return Output Stream contains the execution result of the sub_call.
     */
    OutputStream getOutput();

    /**
     * The input stream for the sub_call command.
     * @return Input stream inside the sub_call command.
     */
    InputStream  getInput();

    /**
     * Output stream turned into ArrayList format.
     * @return ArrayList of arguments of String type.
     * @throws JshException Exception that could be thrown by Java.
     */
    ArrayList<String> get_OutputArray() throws JshException;

}
