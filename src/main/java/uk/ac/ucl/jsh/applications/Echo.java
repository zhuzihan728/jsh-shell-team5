package uk.ac.ucl.jsh.applications;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import uk.ac.ucl.jsh.toolkit.JshException;




public class Echo implements Application{
    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        try{
            if (!appArgs.isEmpty()) {
                String out = String.join(" ",appArgs);
                writer.write(out);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }

        }
        catch (IOException e) {throw new JshException("echo: "+ e.getMessage());}
                
    }
}