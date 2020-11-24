package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.command.Command;
import uk.ac.ucl.jsh.parser.CmdLineParser;
import uk.ac.ucl.jsh.parser.JshCaller;
import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.OutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JshMain {
    public static void runJsh(String cmdLine, OutputStream output) {
        CmdLineParser parser = new CmdLineParser(cmdLine);
        parser.parse();
        Command cmdline = parser.getCmdLine();
        try {
            cmdline.eval(new JshCaller(), System.in, output);
        } catch (JshException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("jsh: wrong number of arguments");
                return;
            }
            if (!args[0].equals("-c")) {
                System.out.println("jsh: " + args[0] + ": unexpected argument");
            }
            try {
                runJsh(args[1], System.out);
            } catch (Exception e) {
                System.out.println("jsh: " + e.getMessage());
            }
        } else {
            Scanner input = new Scanner(System.in);
            try {
                while (true) {
                    String prompt = WorkingDr.getInstance().getWD() + "> ";
                    System.out.print(prompt);
                    try {
                        String cmdline = input.nextLine();
                        runJsh(cmdline, System.out);
                    } catch (NoSuchElementException e){
                        System.err.println(e.getMessage());
                        break;
                    } catch (Exception e) {
                        System.err.println("jsh: " + e.getMessage());
                    }
                }
            } finally {
                input.close();
            }
        }
    }
}
