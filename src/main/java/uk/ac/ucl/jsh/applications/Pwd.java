package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import uk.ac.ucl.jsh.tools.WorkingDr;



public class Pwd implements Application{

    
	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        writer.write(WorkingDr.getInstance().getWD());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }
}