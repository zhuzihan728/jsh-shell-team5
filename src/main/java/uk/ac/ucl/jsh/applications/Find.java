package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.File;

import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.PatternMatcher;
import uk.ac.ucl.jsh.toolkit.WorkingDr;



public class Find implements Application{
    private String path = "";
    private String pattern;
    private void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size() < 2) {
            throw new JshException("find: missing arguments");
        }
        else if (appArgs.size() == 2){
            if (!appArgs.get(0).equals("-name")){
                throw new JshException("find: wrong argument " + appArgs.get(0));
            }
            pattern = appArgs.get(1).replace("*", ".*");
        }
        else if (appArgs.size() == 3){
            if (!appArgs.get(1).equals("-name")){
                throw new JshException("find: wrong argument " + appArgs.get(1));
            }
            path = appArgs.get(0);
            pattern = appArgs.get(2).replace("*", ".*");
        }
        else if (appArgs.size() > 3) {
            throw new JshException("find: too many arguments");
        }
    }




	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        File file = new File(WorkingDr.getInstance().getWD(),path);              
        try {
            boolean atLeastOnePrinted = getFiles(file, pattern, writer);                        
            if (atLeastOnePrinted) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
            else{
                throw new JshException("find: no such file or directory");
            }
        }  catch (IOException e) {
            throw new JshException("find: " + e.getMessage());
        }
    }
    
    private boolean getFiles(File file, String pattern, OutputStreamWriter writer) throws IOException {
        boolean printed1 = false;
        boolean printed2 = false;
        File[] files = file.listFiles();
        for (File a : files) {
            if(a.getName().startsWith(".")){continue;}
            if (PatternMatcher.matchPattern(a.getName(), pattern)) {
                writer.write(getRelative(a));
                writer.write("\n");
                writer.flush();
                printed1 = true;            
            }      
            if(a.isDirectory()){
                if(getFiles(a,pattern,writer)){printed2=true;}
              
            }  
        }
        return (printed1 || printed2);
    }

    private String getRelative(File child){
        String rp = Paths.get(WorkingDr.getInstance().getWD()).relativize(Paths.get(child.getAbsolutePath())).toString();
        if(rp.startsWith(File.separator)){
            return "."+ rp;
        }
        else{
            return "."+ File.separator + rp;
        }
    }

}