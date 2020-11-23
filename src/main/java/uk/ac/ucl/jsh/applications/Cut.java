package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.PatternMatcher;


public class Cut implements Application{

    private String cutArg = null;
    private String option;

    private void checkArguements(ArrayList<String> appArgs, InputStream input) throws JshException {
        if (appArgs.size() < 2) {
            throw new JshException("cut: missing arguments");
        }
        else if (appArgs.size() == 2){
            if (!appArgs.get(0).equals("-b")){
                throw new JshException("cut: wrong argument " + appArgs.get(0));
            }
            else if (input == null){
                throw new JshException("cut: missing InputStream");
            }
        }
        else if (appArgs.size() == 3){
            if (!appArgs.get(0).equals("-b")){
                throw new JshException("cut: wrong argument " + appArgs.get(0));
            }
            cutArg = appArgs.get(2);
        }
        else if (appArgs.size() > 3) {
            throw new JshException("cut: too many arguments");
        }

        String cutpat = "((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-))(,((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-)))*";
        option = appArgs.get(1);
        if (!PatternMatcher.matchPattern(option, cutpat)){
            throw new JshException("cut: wrong argument " + option);
        }
    }
    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws JshException {
        checkArguements(appArgs, input);
        OutputStreamWriter writer = new OutputStreamWriter(output);
        if(cutArg == null){
            try {
                writeToShell(new Scanner(input), writer, option);
            } catch (IOException e) {
                throw new JshException("cut: " + e.getMessage());
            }
        }
        else{
            try{
                writeToShell(InputReader.file_reader(cutArg), writer, option);
            }
            catch(IOException e){throw new JshException("cut: " + e.getMessage());}
        }        
    }


    static ArrayList<Integer> optionSplit(String option){
        String[] optionls = option.split(",");
        ArrayList<Integer> cutls = new ArrayList<Integer>();
        Integer toinf = 0;
        Integer from;
        Integer to; 
        ArrayList<Integer> out = new ArrayList<Integer>();
        for (String a : optionls){
            if(!a.contains("-")){
                from = Integer.parseInt(a);
                if(from>=toinf&&toinf!=0){
                    continue;
                }
                cutls.add(from);              
            }
            else{
                if(a.startsWith("-")){
                    to = Integer.parseInt(a.split("-")[1]);
                    if(to>=toinf&&toinf!=0){
                        return out = null;
                    }
                    for (int i=1; i<=to;i++){
                        cutls.add(i);

                    }                    
                }
                else if(a.endsWith("-")){
                    from = Integer.parseInt(a.split("-")[0]);
                    if(from>=toinf&&toinf!=0){
                        continue;
                    }
                    cutls.add(from);
                    if(!cutls.remove(toinf)){
                        cutls.add(Integer.MAX_VALUE);
                    }
                    toinf = from;                    
                }
                else{
                    String[] range = a.split("-");
                    from = Integer.parseInt(range[0]);
                    to = Integer.parseInt(range[1]);
                    for (int i=from; i<=to;i++){
                        if(i>=toinf&&toinf!=0){
                            break;
                        }
                        cutls.add(i);
                    }                    

                }


            }
        }
        cutls.sort(Integer::compareTo);
        for (Integer a : cutls){
            if(out.contains(a)){
                continue;
            }
            out.add(a);

        }
        return out;
    }

    static void writeToShell(Scanner in, OutputStreamWriter writer, String option)  throws IOException{
        ArrayList<Integer> cutnum = optionSplit(option);
        Integer toinf = 0;
        if(cutnum.contains(Integer.MAX_VALUE)){
            toinf = cutnum.get(cutnum.size()-2);
        }
        String input;
        String output;
        while(in.hasNext()) {
            output = "";
            input = in.nextLine();
            for (Integer i:cutnum){
                if(i > input.length()){
                    break;
                }
                if(i!=toinf){
                    output = output + String.valueOf(input.charAt(i-1));
                }
                else{
                    output = output + input.substring(i);
                    break;
                }

            }
            writer.write(output);
            writer.write(System.getProperty("line.separator"));
            writer.flush();        
        }

    }

}