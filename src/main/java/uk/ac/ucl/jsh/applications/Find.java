package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.ucl.jsh.tools.WorkingDr;

import java.io.File;

public class Find implements Application{

	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        File path;
        String pattern;
        if (appArgs.isEmpty() || appArgs.size() == 1) {
            throw new RuntimeException("find: missing argument");}              
        else if (appArgs.size() == 2){
            if(appArgs.get(0).equals("-name")){
                path = new File(WorkingDr.getInstance().getWD());
                pattern = appArgs.get(1).replaceAll("\\*", ".*");
            }
            else{
                throw new RuntimeException("find: wrong arguments");}                
        }
        else if (appArgs.size() == 3 ){
            if(appArgs.get(1).equals("-name")){
                path = new File(appArgs.get(0));
                pattern = appArgs.get(2).replaceAll("\\*", ".*");
            }
            else{
                throw new RuntimeException("find: wrong arguments");} 
        }
        else {
            throw new RuntimeException("find: too many arguments");
        }
/*                      ↑上面确定path和pattern                     */               
        try {
            boolean atLeastOnePrinted = getFiles(path, pattern, path, writer);
                            
            if (atLeastOnePrinted) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
            else{
                throw new RuntimeException("find: no such file or directory");
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("find: no such directory");
        }
    }
    static boolean getFiles(File file, String pattern, File path, OutputStreamWriter writer) throws IOException {
        boolean printed1 = false;
        boolean printed2 = false;
        File[] files = file.listFiles();
        for (File a : files) {
            if(a.getName().startsWith(".")){continue;}
            if (matchPattern(a.getName(), pattern)) {
                writer.write(getRelative(path,a));
                writer.write("\n");
                writer.flush();
                printed1 = true;            
            }      
            if(a.isDirectory()){
                if(getFiles(a,pattern,path,writer)){printed2=true;}
              
            }  
        }

        return (printed1 || printed2);
    }

    static boolean matchPattern(String name, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    static String getRelative(File mother, File child){//需要检验
        String rp = Paths.get(mother.getAbsolutePath()).relativize(Paths.get(child.getAbsolutePath())).toString();
        if(rp.startsWith(File.separator)){
            return "."+ rp;
        }
        else{
            return "."+ File.separator + rp;
        }
    }
}