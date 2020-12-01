package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.JshMain;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Substitution implements Sub_Call{
    private final String substitution_string;
    private OutputStream substitution_output;

    public Substitution(String substitution){
        this.substitution_string = substitution.substring(1,substitution.length()-1);
        get_output();
    }

    private void get_output() {
        substitution_output = new ByteArrayOutputStream();
        JshMain.runJsh(substitution_string, substitution_output);
    }

    public OutputStream compute(){
        return substitution_output;
    }

    @Override
    public String getString() {
        return substitution_output.toString().trim();
    }

    @Override
    public String getType() {
        return "Substitution";
    }

    @Override
    public OutputStream getOutput() {
        return substitution_output;
    }

    @Override
    public InputStream getInput() {
        return null;
    }

    @Override
    public ArrayList<String> get_OutputArray() {
        return new ArrayList<>(Arrays.asList(substitution_output.toString().trim().split(System.getProperty("line.separator"))));
    }
}
