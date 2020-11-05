package uk.ac.ucl.jsh;

public interface CommandCaller {
    public String call(Pipe pipe);
    public String call(Sequence sequence);
    public String call(Call call);
}
