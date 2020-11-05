package uk.ac.ucl.jsh;

import org.antlr.v4.runtime.tree.ParseTree;

import java.io.OutputStream;
import java.util.Scanner;

public class JshMain {
    private static void runJsh(String cmdLine, OutputStream output){
        CmdLineParser parser = new CmdLineParser(cmdLine);
        ParseTree tree = parser.getParseTree();
        Command command = new Command(tree);
        command.eval(command,output);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String cmdline = input.nextLine();
        runJsh(cmdline, System.out);
    }
}
