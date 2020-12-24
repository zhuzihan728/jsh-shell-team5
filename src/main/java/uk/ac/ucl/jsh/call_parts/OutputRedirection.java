package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.InputReader;

import java.io.*;
import java.util.ArrayList;

/**
 * Concrete class that deal with output redirection type of Sub_call.
 */
public class OutputRedirection implements Sub_Call{
    /**
     * Arguments received when constructing an instance of the class.
     */
    private final ArrayList<String> arguments;
    /**
     * Output Stream that specified by this output redirection.
     */
    public OutputStream output;

    /**
     * Construction class that turns the received commands of type Subcall into type String.
     * @param argus Received list of arguments of type Sub_call.
     */
    public OutputRedirection(ArrayList<Sub_Call> argus) {
        this.arguments = new ArrayList<>();
        for(Sub_Call call: argus){
            this.arguments.add(call.getString());
        }
        getInputStream();
    }


    /**
     * Get the specified output stream indicated in the arugment.
     */
    private void getInputStream(){
        try {
            output = new FileOutputStream(InputReader.getFile(arguments.get(0)));
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public String getString() {
        return null;
    }

    /**
     * Returns the type name of this concrete class.
     */
    @Override
    public String getType() {
        return "OutputRedirection";
    }

    /**
     * Getter function for the Output Stream that specified by this output redirection.
     */
    @Override
    public OutputStream getOutput() {
        return output;
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public InputStream getInput() {
        return null;
    }

    /**
     * Function needs implementation but not used. So return null.
     */
    @Override
    public ArrayList<String> get_OutputArray()  {
        return null;
    }

}
