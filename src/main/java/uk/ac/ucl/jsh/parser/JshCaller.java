package uk.ac.ucl.jsh.parser;

import uk.ac.ucl.jsh.parser.CommandCaller;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;
import uk.ac.ucl.jsh.parser.CmdLineParser;
import uk.ac.ucl.jsh.toolkit.AppMaker;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JshCaller implements CommandCaller {

    @Override
    public void call(Pipe pipe, InputStream inputStream, OutputStream output) throws JshException {
        ByteArrayOutputStream firstOutputStream = new ByteArrayOutputStream();
        pipe.getLeft().eval(this, inputStream, firstOutputStream);
        ByteArrayInputStream secondInputStream = new ByteArrayInputStream(firstOutputStream.toByteArray());
        pipe.getRight().eval(this, secondInputStream, output);
    }

    @Override
    public void call(Sequence sequence, InputStream inputStream, OutputStream output) throws JshException {
        sequence.getFirst().eval(this, inputStream, output);
        sequence.getSecond().eval(this, inputStream, output);
    }

    @Override
    public void call(Call call, InputStream input, OutputStream output) throws JshException {
        ArrayList<Sub_Call> tokens = CmdLineParser.getTokens(call.getString());
        InputStream call_input = input;
        OutputStream call_output = output;
        ArrayList<String> arguments = new ArrayList<>();
        for(Sub_Call sub_call : tokens){
            switch (sub_call.getType()){
                case("Substitution"):
                case("BaseCall"):
                    arguments.addAll(sub_call.get_OutputArray());
                    break;
                case("OutputRedirection"):
                    call_output = sub_call.getOutput();
                    break;
                case("InputRedirection"):
                    call_input = sub_call.getInput();
                    break;
            }
        }
        AppMaker.getInstance().makeApp(arguments.get(0)).exec(new ArrayList<>(arguments.subList(1, arguments.size())), call_input, call_output);
    }

}