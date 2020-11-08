package uk.ac.ucl.jsh.applications;

import uk.ac.ucl.jsh.applications.Application;

import java.util.ArrayList;
import java.lang.String;
import java.io.File;
import java.io.OutputStream;


public class Ls implements Application {
    public Ls(){

    }
    public void exec(ArrayList<String> args, OutputStream output){
        File currDir;
        if (args.isEmpty()) {
            currDir = new File(currentDirectory);
        } else if (args.size() == 1) {
            currDir = new File(args.get(0));
        } else {
            throw new RuntimeException("ls: too many arguments");
        }
        try {
            File[] listOfFiles = currDir.listFiles();
            boolean atLeastOnePrinted = false;
            for (File file : listOfFiles) {
                if (!file.getName().startsWith(".")) {
                    writer.write(file.getName());
                    writer.write("\t");
                    writer.flush();
                    atLeastOnePrinted = true;
                }
            }
            if (atLeastOnePrinted) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("ls: no such directory");
        }   
    }
}