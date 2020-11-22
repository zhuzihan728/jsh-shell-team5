package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.File;

public class Cd implements Application{

    private void checkArguements(ArrayList<String> appArgs) throws JshException {
        if (appArgs.isEmpty()) {
            throw new JshException("cd: missing arguement");
        }
        else if (appArgs.size() > 1){
            throw new JshException("cd: too many arguements");
        }
    }


	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs);
        String dirString = appArgs.get(0);
        File dir = new File(WorkingDr.getInstance().getWD(), dirString);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new JshException("cd: " + dirString + " is not an existing directory");
        }
        try {
            WorkingDr.getInstance().setWD(dir.getCanonicalPath());
        } catch (IOException e) {
            throw new JshException("cd: " + e.getMessage());
        }

    }
 
}