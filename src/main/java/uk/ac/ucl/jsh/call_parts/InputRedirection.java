package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.InputReader;

import java.io.*;
import java.util.ArrayList;

/**
 * Concrete class that deal with input redirection type of Sub_call.
 */
public class InputRedirection implements Sub_Call{
    /**
     * Arguments received when constructing an instance of the class.
     */
    private final ArrayList<String> arguments;

    /**
     * Input Stream that specified by this input redirection.
     */
    private InputStream input;

    /**
     * Construction class that turns the received commands of type Subcall into type String.
     * @param arguments Received list of arguments of type Sub_call.
     */
    public InputRedirection(ArrayList<Sub_Call> arguments){
        this.arguments = new ArrayList<>();
        for(Sub_Call call: arguments){
            this.arguments.add(call.getString());
        }
        getInputStream();
    }

    /**
     * Get the specified input stream indicated in the arugment.
     */
    private void getInputStream(){
        try {
            input = new FileInputStream(InputReader.getFile(arguments.get(0)));
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Getter function for the input Stream that specified by this input redirection.
     */
    public InputStream getInput(){
        return input;
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public ArrayList<String> get_OutputArray()  {
        return null;
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    public String getString(){
        return null;
    }

    /**
     * Returns the type name of this concrete class.
     */
    @Override
    public String getType() {
        return "InputRedirection";
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public OutputStream getOutput() {
        return null;
    }

}
