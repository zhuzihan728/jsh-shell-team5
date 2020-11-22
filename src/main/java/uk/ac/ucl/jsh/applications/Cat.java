package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;




public class Cat implements Application{

    private void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.isEmpty() && input == null) {
            throw new JshException("cat: missing InputStream");
        }
    }

    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
         
        if (appArgs.isEmpty()) {
            try{
                writeToShell(new Scanner(input), writer);
            }
            catch(IOException e){
                throw new JshException("cat: " + e.getMessage());
            }
        } 
        else {
            for (String arg : appArgs) {
                try {
                    writeToShell(InputReader.file_reader(arg), writer);
                }           
                catch (IOException e) {throw new JshException("cat: " + e.getMessage());}
            }
        }
    }

    private void writeToShell(Scanner in, OutputStreamWriter writer) throws IOException {
        String line;
        while (in.hasNextLine()) {
            line = in.nextLine();
            writer.write(String.valueOf(line));
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
}