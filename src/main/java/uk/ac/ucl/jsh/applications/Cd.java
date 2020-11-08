package uk.ac.ucl.jsh.applications;

import java.util.ArrayList;
import java.lang.String;
import java.io.File;
import java.io.OutputStream;


public class Cd implements Application{
    public Cd(){

    }
    public void exec(ArrayList<String> args, OutputStream output){
        if (args.isEmpty()) {
            throw new RuntimeException("cd: missing argument");
        } else if (args.size() > 1) {
            throw new RuntimeException("cd: too many arguments");
        }
        String dirString = args.get(0);
        File dir = new File(currentDirectory, dirString);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("cd: " + dirString + " is not an existing directory");
        }
        currentDirectory = dir.getCanonicalPath();
    }
}