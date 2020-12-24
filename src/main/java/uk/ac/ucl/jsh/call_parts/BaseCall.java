package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.Globbing;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Concrete class that deal with BaseCall type of Sub_call.
 * A BaseCall does not contain any operators for redirection, sequence or pipe.
 */
public class BaseCall implements Sub_Call{
    /**
     * Content of the BaseCall type of Sub_call.
     */
    private final String call;
    /**
     * Turn BaseCall into list of arguements.
     */
    private ArrayList<String> globbed_results;
    /**
     * Type integer indicates whether the command is a content of single quoted or 
     * double quoted.
     */
    private final int type;

    /**
     * Construction class that constructs an instance of the BaseCall class.
     * @param call Call content of the base call.
     * @param type Integer indicates type of the base call.
     */
    public BaseCall(String call,int type) {
        this.call = call;
        this.type = type;
        check_String();
    }

    /**
     * Turn a raw baseCall string into arraylist of arguments according to the string type integer, apply globbing if needed.
     * Type == 1: String is non-quoted so if there's * , it should be globbed.
     * Type == 2: Single quoted string that needs to drop the quotes.
     * Else type = 0: String part inside the double quotes.
     */
    private void check_String(){
        globbed_results = new ArrayList<>();
        if (this.type == 1 && call.contains("*")){
            Globbing globbing = new Globbing(call);
            this.globbed_results = globbing.getGlobbed_results();
        }
        else if (this.type == 2) {
            this.globbed_results.add(call.substring(1, call.length() - 1));     
        }
        else{
            this.globbed_results.add(call);
        }
    }

    /**
     * Getter function for the globbed results in string format.
     */
    @Override
    public String getString() {
        return arrayToString();
    }

    /**
     * Returns the type name of this concrete class.
     */
    @Override
    public String getType() {
        return "BaseCall";
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public OutputStream getOutput() {
        return null;
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public InputStream getInput() {
        return null;
    }

    /**
     * Get the list of strings that the raw string command turned to.
     */
    @Override
    public ArrayList<String> get_OutputArray(){
        return globbed_results;
    }

    /**
     * Helper function that turns an arraylist of strings into a single string.
     * @return The globbed results in string format
     */
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
