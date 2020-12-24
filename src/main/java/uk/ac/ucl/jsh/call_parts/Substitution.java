package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.JshMain;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Concrete class for Substitution type of sub_call.
 */
public class Substitution implements Sub_Call{
    /**
     * String content inside the substitution sub_call.
     */
    private final String substitution_string;

    /**
     * Output Stream that the substitution subcall writes to.
     */
    private OutputStream substitution_output;

    /**
     * An instance of this class constructed using the string content.
     * @param substitution String content quoted by back quotes.
     */
    public Substitution(String substitution){
        this.substitution_string = substitution.substring(1,substitution.length()-1);
        get_output();
    }

    /**
     * Method for executing the substituted content and get output.
     */
    private void get_output() {
        substitution_output = new ByteArrayOutputStream();
        JshMain.runJsh(substitution_string, substitution_output);
    }

    /**
     * Getter function for getting the output stream that the execution of the substituted 
     * String wrote to.
     * 
     * @return The output stream which contains the execution result of running the substituted string.
     */
    public OutputStream compute(){
        return substitution_output;
    }

    /**
     * Get the output of the substituted content in a single String format.
     */
    @Override
    public String getString() {
        return substitution_output.toString().replace(System.getProperty("line.separator"),"");
    }

    /**
     * Returns the type name of this concrete class.
     */
    @Override
    public String getType() {
        return "Substitution";
    }

    /**
     * Returns the output stream that execution of substituted content wrote to. 
     */
    @Override
    public OutputStream getOutput() {
        return substitution_output;
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public InputStream getInput() {
        return null;
    }

    /**
     * Getter function of the output stream in ArrayList format.
     */
    @Override
    public ArrayList<String> get_OutputArray() {
        return new ArrayList<>(Arrays.asList(substitution_output.toString().split(System.getProperty("line.separator"))));
    }
}
