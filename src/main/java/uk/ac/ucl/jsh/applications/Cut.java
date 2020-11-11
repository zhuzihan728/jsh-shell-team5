package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.PatternMatcher;


public class Cut implements Application{
    
	@Override
    public void exec(ArrayList<String> appArgs, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        String cutpat = "((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-))(,((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-)))*";
        if (appArgs.size() < 2) {
            throw new RuntimeException("cut: missing arguments");
        }
        else if (appArgs.size() == 2){
            if(appArgs.get(0).equals("-b")){
                String option = appArgs.get(1);
                    if (PatternMatcher.matchPattern(option, cutpat)){
                        stdinToShell(writer, option);
                    }
                    else{
                        throw new RuntimeException("cut: wrong option argument");
                    }
            }
            else{
                throw new RuntimeException("cut: wrong argument");
            }

        }
        else if (appArgs.size() == 3){
            if(appArgs.get(0).equals("-b")){
                String option = appArgs.get(1);
                if (PatternMatcher.matchPattern(option, cutpat)){
                    String cutArg = appArgs.get(2);
                    try{cutfileToShell(InputReader.read_file(cutArg), writer, option);}
                    catch(IOException e){throw new RuntimeException("uniq: cannot open " + cutArg);}
                    catch(RuntimeException e){throw new RuntimeException("uniq: " + cutArg + " does not exist");}
                }
                else{
                    throw new RuntimeException("cut: wrong option argument");
                }
            }
            else{
                throw new RuntimeException("cut: wrong arguments");
            }

        }
        else {
            throw new RuntimeException("uniq: too many arguments");
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

    static void stdinToShell(OutputStreamWriter writer, String option)  throws IOException{
        Scanner in = new Scanner(System.in);//do not close
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

    static void cutfileToShell(ArrayList<String> file, OutputStreamWriter writer, String option)  throws IOException{
        ArrayList<Integer> cutnum = optionSplit(option);
        Integer toinf = 0;
        if(cutnum.contains(Integer.MAX_VALUE)){
            toinf = cutnum.get(cutnum.size()-2);
        }
        String output;
        for(String input : file) {
            output = "";
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