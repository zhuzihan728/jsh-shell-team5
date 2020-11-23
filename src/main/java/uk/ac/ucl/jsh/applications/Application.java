package uk.ac.ucl.jsh.applications;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.ac.ucl.jsh.toolkit.JshException;

public interface Application{
    public void exec(ArrayList<String> args, InputStream input, OutputStream output) throws JshException;
}