package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

interface Application{
    public void exec(ArrayList<String> args, OutputStream output) throws IOException;
}