package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;

import java.io.InputStream;
import java.io.OutputStream;

public class JshCaller implements CommandCaller{

    @Override
    public String call(Pipe pipe, InputStream inputStream, OutputStream output) {
        return null;
    }

    @Override
    public String call(Sequence sequence,InputStream inputStream, OutputStream output) {
        return null;
    }

    @Override
    public String call(Call call,InputStream inputStream, OutputStream output) {
        return null;
    }

}
