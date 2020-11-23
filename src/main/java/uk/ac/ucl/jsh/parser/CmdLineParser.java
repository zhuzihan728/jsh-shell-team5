package uk.ac.ucl.jsh.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import uk.ac.ucl.jsh.CallGrammarLexer;
import uk.ac.ucl.jsh.CallGrammarParser;
import uk.ac.ucl.jsh.JshGrammarLexer;
import uk.ac.ucl.jsh.JshGrammarParser;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.command.Command;

import java.util.ArrayList;

public class CmdLineParser{
    private String cmdLine;
    private JshGrammarParser.CmdlineContext parseTree;
    private Command command;

    public CmdLineParser(String cmdLine){
        this.cmdLine = cmdLine;
    }

    public void parse(){
        parseCmdLine(cmdLine);
    }

    private void parseCmdLine(String cmdline){
        CharStream parserInput = CharStreams.fromString(cmdline);
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        parseTree = parser.cmdline();
        command = new CommandVisitor().visitCmdline(parseTree);
    }

    public Command getCmdLine(){
        return command;
    }

    public static ArrayList<Sub_Call> getTokens(String str){
        CharStream parserInput = CharStreams.fromString(str);
        CallGrammarLexer lexer = new CallGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CallGrammarParser parser = new CallGrammarParser(tokenStream);
        CallGrammarParser.CallContext parseTree = parser.call();
        ArrayList<Sub_Call> tokens = new CallVisitor().visitCall(parseTree);
        return tokens;
    }

}
