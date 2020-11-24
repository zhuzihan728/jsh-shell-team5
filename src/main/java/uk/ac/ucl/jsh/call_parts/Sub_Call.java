package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public interface Sub_Call {

    public String getString();

    public String getType();

    public OutputStream getOutput();

    public InputStream  getInput();

    public ArrayList<String> get_OutputArray() throws JshException;

}
