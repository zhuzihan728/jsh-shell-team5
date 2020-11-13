package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface CommandCaller {
    public Void call(Pipe pipe, InputStream inputStream, OutputStream output) throws IOException;
    public Void call(Sequence sequence,InputStream inputStream, OutputStream output) throws IOException;
    public Void call(Call call,InputStream inputStream, OutputStream output) throws IOException;
}
