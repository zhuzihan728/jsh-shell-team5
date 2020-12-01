package uk.ac.ucl.jsh.call_parts;

import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public interface Sub_Call {

    String getString();

    String getType();

    OutputStream getOutput();

    InputStream  getInput();

    ArrayList<String> get_OutputArray() throws JshException;

}
