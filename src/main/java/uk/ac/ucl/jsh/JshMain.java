package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.command.Command;
import uk.ac.ucl.jsh.parser.CmdLineParser;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class JshMain {
    public static void runJsh(String cmdLine, OutputStream output) throws IOException {
        CmdLineParser parser = new CmdLineParser(cmdLine);
        parser.parse();
        Command cmdline = parser.getCmdLine();
        cmdline.eval(new JshCaller(), System.in,output);
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
                    } catch (Exception e) {
                        System.err.println("jsh: " + e.getMessage());
                        break;
                    }
                }
            } finally {
                input.close();
            }
        }
    }
}
