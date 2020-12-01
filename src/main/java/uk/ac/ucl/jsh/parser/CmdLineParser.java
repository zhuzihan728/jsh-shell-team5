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

public class CmdLineParser{
    private final String cmdLine;
    private Command command;
    private ArrayList<Sub_Call> tokens;

    public CmdLineParser(String cmdLine){
        this.cmdLine = cmdLine;
    }

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

    public Command getCmdLine(){
        parseCmdLine();
        return command;
    }

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

    public ArrayList<Sub_Call> getTokens(){
        parseTokens();
        return this.tokens;
    }


}
