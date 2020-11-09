package uk.ac.ucl.jsh.applications;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import uk.ac.ucl.jsh.tools.WorkingDr;


public class Sort implements Application{
    
	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if (appArgs.isEmpty()) {
            writeToShell(read_stdin(), writer, false);
        }
        else if (appArgs.size() == 1) {
            String sortArg = appArgs.get(0);
            if(sortArg.equals("-r")){
                writeToShell(read_stdin(), writer, true);
            }
            else {
                Path curp = Paths.get(WorkingDr.getInstance().getWD());
                Path p = curp.resolve(sortArg);
                if (Files.exists(p)){
                    try{writeToShell(read_file(p), writer, false);}
                    catch(IOException e){throw new RuntimeException("sort: cannot open " + sortArg);}
                }
                else{
                    throw new RuntimeException("sort: " + sortArg + " does not exist");
                }
            }      
        }
        else if (appArgs.size() == 2){
            if(appArgs.get(0).equals("-r")){
                String sortArg = appArgs.get(1);
                Path curp = Paths.get(WorkingDr.getInstance().getWD());
                Path p = curp.resolve(sortArg);
                if (Files.exists(p)){
                    try{writeToShell(read_file(p), writer, true);}
                    catch(IOException e){throw new RuntimeException("sort: cannot open " + sortArg);}
                }
                else{
                    throw new RuntimeException("sort: " + sortArg + " does not exist");
                }
            }
            else{
                throw new RuntimeException("sort: wrong arguments");
            }

        }
        else {
            throw new RuntimeException("sort: too many arguments");
        }                
    }

    static ArrayList<String> read_file(Path file) throws IOException {
        Charset encoding = StandardCharsets.UTF_8;
        BufferedReader reader = Files.newBufferedReader(file,encoding);
        ArrayList<String> listoflines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            listoflines.add(line);
        }
        reader.close();
        return listoflines;
    }

    static void writeToShell(ArrayList<String> listoflines, OutputStreamWriter writer, boolean r) throws IOException {
        listoflines.sort(String::compareTo);
        if (r){
            Collections.reverse(listoflines);
        }
        for(String a:listoflines){
            writer.write(a);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
   
    static ArrayList<String> read_stdin() {
        Scanner in = new Scanner(System.in);//do not close
        ArrayList<String> listoflines = new ArrayList<>();
        while(in.hasNext()) {
            listoflines.add(in.nextLine());
        }
        return listoflines;

    }
}