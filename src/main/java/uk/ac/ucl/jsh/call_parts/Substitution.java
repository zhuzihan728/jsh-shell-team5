package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.JshMain;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class Substitution implements Sub_Call {
    private final String substitution_string;
    private OutputStream substition_output;

    public Substitution(String substitution) {
        this.substitution_string = substitution.substring(1, substitution.length() - 1);
        get_output();
    }

    private void get_output() {
        substition_output = new ByteArrayOutputStream();
        JshMain.runJsh(substitution_string, substition_output);
    }

    public OutputStream compute(){
        return substition_output;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public String getType() {
        return "Substitution";
    }

    @Override
    public OutputStream getOutput() {
        return substition_output;
    }

    @Override
    public InputStream getInput() {
        return null;
    }
}
