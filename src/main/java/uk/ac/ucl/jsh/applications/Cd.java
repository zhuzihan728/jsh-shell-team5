package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.File;

public class Cd implements Application{

	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        if (appArgs.isEmpty()) {
            throw new RuntimeException("cd: missing argument");
        } else if (appArgs.size() > 1) {
            throw new RuntimeException("cd: too many arguments");
        }
        ///////////
        String dirString = appArgs.get(0);
        File dir = new File(WorkingDr.getInstance().getWD(), dirString);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("cd: " + dirString + " is not an existing directory");
        }
        WorkingDr.getInstance().setWD(dir.getCanonicalPath()); 

    }
}