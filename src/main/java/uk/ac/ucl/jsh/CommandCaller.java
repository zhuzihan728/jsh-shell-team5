package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;
import uk.ac.ucl.jsh.toolkit.JshException;
import java.io.InputStream;
import java.io.OutputStream;

public interface CommandCaller {
    public void call(Pipe pipe, InputStream inputStream, OutputStream output) throws JshException;
    public void call(Sequence sequence,InputStream inputStream, OutputStream output) throws JshException;
    public void call(Call call,InputStream inputStream, OutputStream output) throws JshException;
}
