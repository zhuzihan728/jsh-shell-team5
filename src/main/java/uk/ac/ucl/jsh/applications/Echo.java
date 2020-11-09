package uk.ac.ucl.jsh.applications;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;




public class Echo implements Application{
    
	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        boolean atLeastOnePrinted = false;
                for (String arg : appArgs) {
                    writer.write(arg);
                    writer.write(" ");
                    writer.flush();
                    atLeastOnePrinted = true;
                }
                if (atLeastOnePrinted) {
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                }
    }
}