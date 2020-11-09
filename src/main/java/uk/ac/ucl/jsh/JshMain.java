package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.command.Command;
import uk.ac.ucl.jsh.parser.CmdLineParser;

import java.io.InputStream;
import java.util.Scanner;

public class JshMain {
    private static void runJsh(String cmdLine, InputStream input){
        CmdLineParser parser = new CmdLineParser(cmdLine);
        parser.parse();
        Command cmdline = parser.getCmdLine();
        cmdline.eval(cmdline,input, System.out);
    }

    public static void main(String[] args) {
        String cmdline = new Scanner(System.in).nextLine();
        runJsh(cmdline, System.in);
    }
}
