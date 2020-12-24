package uk.ac.ucl.jsh.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import uk.ac.ucl.jsh.CallGrammarLexer;
import uk.ac.ucl.jsh.CallGrammarParser;
import uk.ac.ucl.jsh.JshGrammarLexer;
import uk.ac.ucl.jsh.JshGrammarParser;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.command.Command;
import uk.ac.ucl.jsh.toolkit.ParserErrorHandling;

import java.util.ArrayList;

/**
 * Parser class that turns the raw input command into a class type that
 * defined in JSH.
 */
public class CmdLineParser{
    private final String cmdLine;
    private Command command;
    private ArrayList<Sub_Call> tokens;

    /**
     * 
     * @param cmdLine Raw input command line from the user.
     */
    public CmdLineParser(String cmdLine){
        this.cmdLine = cmdLine;
    }

    /**
     * Parse the raw input command line into a Command defined in JSH.
     */
    private void parseCmdLine(){
        CharStream parserInput = CharStreams.fromString(cmdLine);
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ParserErrorHandling.INSTANCE);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(ParserErrorHandling.INSTANCE);
        JshGrammarParser.CmdlineContext parseTree = parser.cmdline();
        command = new CommandVisitor().visitCmdline(parseTree);
    }

    /**
     * Function allows other part of the code to get the parsed command.
     * 
     * @return Command defined in the command interface.
     */
    public Command getCmdLine(){
        parseCmdLine();
        return command;
    }

    /**
     * Parse the token of type Call into a list of arguments.
     */
    private void parseTokens(){
        CharStream parserInput = CharStreams.fromString(cmdLine);
        CallGrammarLexer lexer = new CallGrammarLexer(parserInput);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ParserErrorHandling.INSTANCE);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CallGrammarParser parser = new CallGrammarParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(ParserErrorHandling.INSTANCE);
        CallGrammarParser.CallContext parseTree = parser.call();
        this.tokens =  new CallVisitor().visitCall(parseTree);
    }

    /**
     * Function allows other part of the code to get the list of parsed tokens.
     * @return Arraylist of arguments of type Sub_Call.
     */
    public ArrayList<Sub_Call> getTokens(){
        parseTokens();
        return this.tokens;
    }


}
