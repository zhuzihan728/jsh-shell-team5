package uk.ac.ucl.jsh.toolkit;

/**
 * Custom Exception Class that all Jsh applications throw
 */
public class JshException extends Exception  {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of a JshException with an exception message 
     * 
     * @param message An exception message as a string
     */
    public JshException(String message) {
        super(message);
    }
}
