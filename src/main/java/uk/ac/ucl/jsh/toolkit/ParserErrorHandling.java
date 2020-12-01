package uk.ac.ucl.jsh.toolkit;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;


public class ParserErrorHandling extends BaseErrorListener{

    public static final ParserErrorHandling INSTANCE = new ParserErrorHandling();
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
