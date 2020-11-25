package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.Globbing;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BaseCall implements Sub_Call{
    private String call;
    private ArrayList<String> globbed_results;
    private int type;

    public BaseCall(String call,int type) {
        this.call = call;
        this.type = type;
    }

    private void check_String() throws JshException {
        globbed_results = new ArrayList<>();
        if (this.type == 1 || this.type == 2) {
            this.globbed_results.add(call.substring(1,call.length()-1));
        }else if(call.contains("*")) {
            Globbing globbing = new Globbing(call);
            if (globbing.exist_globbing()) {
                this.globbed_results = globbing.getGlobbed_results();
            }else{
                this.globbed_results.add(call);
            }
        }else{
            this.globbed_results.add(call);
        }
    }

    @Override
    public String getString() {
        return call;
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
    public ArrayList<String> get_OutputArray() throws JshException {
        check_String();
        return globbed_results;
    }
}
