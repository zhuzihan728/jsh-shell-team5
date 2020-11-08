package uk.ac.ucl.jsh.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import uk.ac.ucl.jsh.JshGrammarLexer;
import uk.ac.ucl.jsh.JshGrammarParser;

public class CmdLineParser{
    private String cmdLine;
    private ParseTree parseTree;

    public CmdLineParser(String cmdLine){
        this.cmdLine = cmdLine;
    }

    public ParseTree getParseTree() {
        return parseTree;
    }

    public void parse(){
        parseCmdLine(cmdLine);
    }

    private void parseCmdLine(String cmdline){
        CharStream parserInput = CharStreams.fromString(cmdline);
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        parseTree = parser.command();
        System.out.println(parseTree.toStringTree(parser));
    }
}
