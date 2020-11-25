package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.File;

public class Ls implements Application{
    private File currDir;
    private void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.isEmpty()) {
            currDir = new File(WorkingDr.getInstance().getWD());
        } else if (appArgs.size() == 1) {
            currDir = InputReader.getFile(appArgs.get(0));
            if (!currDir.exists()){
                throw new JshException("ls: " + appArgs.get(0) + " does not exist");
            }
        } else {
            throw new JshException("ls: too many arguments");
        }
    }


	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
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
        } catch (IOException e){throw new JshException("ls: " + e.getMessage());}
    }
}