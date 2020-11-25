package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.Globbing;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseCall implements Sub_Call{
    private final String call;
    private ArrayList<String> globbed_results;
    private final int type;

    public BaseCall(String call,int type) {
        this.call = call;
        this.type = type;
        check_String();
    }

    private void check_String(){
        globbed_results = new ArrayList<>();
        if (this.type == 2) {
            this.globbed_results.add(call.substring(1, call.length() - 1));
        }else if(call.contains("*")) {
            Globbing globbing = new Globbing(call);
            this.globbed_results = globbing.getGlobbed_results();
        }else{
            this.globbed_results.add(call);
        }
    }

    @Override
    public String getString() {
        return arrayToString();
    }

    @Override
    public String getType() {
        return "BaseCall";
    }

    @Override
    public OutputStream getOutput() {
        return null;
    }

    @Override
    public InputStream getInput() {
        return null;
    }

    @Override
    public ArrayList<String> get_OutputArray(){
        return globbed_results;
    }

    private String arrayToString(){
        StringBuilder builder = new StringBuilder();
        for(String str: globbed_results){
            builder.append(str).append(' ');
        }
        String returns = builder.toString();
        returns = returns.substring(0,returns.length()-1);
        return returns;
    }


}
