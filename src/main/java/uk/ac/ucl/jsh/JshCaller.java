package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.applications.AppFactory;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;
import uk.ac.ucl.jsh.parser.CmdLineParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JshCaller implements CommandCaller{

    @Override
    public Void call(Pipe pipe, InputStream inputStream, OutputStream output) throws IOException {
        ByteArrayOutputStream firstOutputStream = new ByteArrayOutputStream();
        pipe.getLeft().eval(this,inputStream,firstOutputStream);
        ByteArrayInputStream secondInputStream = new ByteArrayInputStream(
                firstOutputStream.toByteArray());
        pipe.getRight().eval(this,secondInputStream,output);
        return null;
    }

    @Override
    public Void call(Sequence sequence,InputStream inputStream, OutputStream output) throws IOException {
        sequence.getFirst().eval(this,inputStream,output);
        sequence.getSecond().eval(this,inputStream,output);
        return null;
    }

    @Override
    public Void call(Call call,InputStream input, OutputStream output) throws IOException {
        ArrayList<Sub_Call> tokens = CmdLineParser.getTokens(call.getString());
        InputStream call_input = input;
        OutputStream call_output = output;
        ArrayList<String> arguments = new ArrayList<>();
        for(Sub_Call sub_call : tokens){
            switch (sub_call.getType()){
                case("Substitution"):
                    OutputStream outputStream = sub_call.getOutput();
                    arguments.addAll(new ArrayList<>(Arrays.asList(outputStream.toString().trim().split(System.getProperty("line.separator")))));
                    break;
                case("OutputRedirection"):
                    call_output = sub_call.getOutput();
                    break;
                case("InputRedirection"):
                    call_input = sub_call.getInput();
                    break;
                case("BaseCall"):
                    arguments.add(sub_call.getString());
                    break;
            }
        }
        AppFactory af =  new AppFactory();
        af.getApplication(arguments.get(0))
                .exec(new ArrayList<>(arguments.subList(1, tokens.size()-1)), call_input, call_output);
        return null;
    }

}
