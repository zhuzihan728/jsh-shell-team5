package uk.ac.ucl.jsh.toolkit;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * Class used to wrap the error from the Antlr plugin into a format that provides more information.
 */
public class ParserErrorHandling extends BaseErrorListener{

    /**
     * This public field provides an instance of this class which allows this class to be
     * used without instantiate a class.
     */
    public static final ParserErrorHandling INSTANCE = new ParserErrorHandling();

    /**
     * Method used to wrap up the default error message.
     * 
     * @param msg The raw command that cannot be parsed.
     * @param charPositionInLine The position of the character where the error occurs.
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e)
            throws ParseCancellationException {
        String[] strings = msg.split("\\s+");
        throw new ParseCancellationException("Syntax error near unexpected token at "
                + strings[strings.length-1].charAt(charPositionInLine));
    }

}
