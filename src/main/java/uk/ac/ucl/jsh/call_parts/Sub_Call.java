package uk.ac.ucl.jsh.call_parts;

import java.io.InputStream;
import java.io.OutputStream;

public interface Sub_Call {

    public String getString();

    public String getType();

    public OutputStream getOutput();

    public InputStream  getInput();

}
