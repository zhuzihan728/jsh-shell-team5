package uk.ac.ucl.jsh;

import org.junit.Test;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Command;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;
import uk.ac.ucl.jsh.parser.CmdLineParser;
import uk.ac.ucl.jsh.toolkit.JshException;

import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ParserTest {

    private ArrayList<String> sub_call_to_string(ArrayList<Sub_Call> sub_calls) throws JshException {
        ArrayList<String> result = new ArrayList<>();
        for(Sub_Call sub_call : sub_calls){
            ArrayList<String> array = sub_call.get_OutputArray();
            if(array != null) {
                result.addAll(sub_call.get_OutputArray());
            }
        }
        return result;
    }

    @Test
    public void testUnquoted() throws JshException {
        String test_input = "a   bc  def";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a","bc","def"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertTrue(expected_out.equals(sub_call_to_string(parser_out)));
    }

    @Test
    public void testSingleQuoted() throws JshException {
        String test_input = "echo a'a b c `echo def`' ";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","aa b c `echo def`"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertTrue(expected_out.equals(sub_call_to_string(parser_out)));
    }

    @Test
    public void testDoubleQuoted() throws JshException {
        String test_input = "echo \"hi\" \"how `echo are`\"";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","hi","how are"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertTrue(expected_out.equals(sub_call_to_string(parser_out)));
    }

    @Test
    public void testBackQuoted() throws JshException {
        String test_input = "echo `echo hello` ";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","hello"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertTrue(expected_out.equals(sub_call_to_string(parser_out)));
    }

    @Test
    public void testRedirection1() throws JshException {
        String test_input = "a > b";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertTrue(expected_out.equals(sub_call_to_string(parser_out)));
    }

    @Test
    public void testRedirection2() throws JshException {
        String test_input = "a < b";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertTrue(expected_out.equals(sub_call_to_string(parser_out)));
    }

    @Test
    public void testRedirection3() throws JshException {
        String test_input = "a < b > c";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertTrue(expected_out.equals(sub_call_to_string(parser_out)));
    }

    @Test
    public void testCall(){
        String test_input = "a b";
        Command expected = new Call("a b");
        Command actual = new CmdLineParser(test_input).getCmdLine();
        assertEquals(expected.getString(),actual.getString());
    }

    @Test
    public void testPipe(){
        String test_input = "a|b";
        Command expected = new Pipe(new Call("a"), new Call("b"));
        Command actual = new CmdLineParser(test_input).getCmdLine();
        assertEquals(expected.getString(),actual.getString());
    }

    @Test
    public void testSeq(){
        String test_input = "a;b";
        Command expected = new Sequence(new Call("a"), new Call("b"));
        Command actual = new CmdLineParser(test_input).getCmdLine();
        assertEquals(expected.getString(),actual.getString());
    }

    @Test
    public void testComplexCommand(){
        String test_input = "a|b;c|d";
        Command expected = new Sequence(
                new Pipe(new Call("a"), new Call("b")) , new Pipe(new Call("c"), new Call("d")));
        Command actual = new CmdLineParser(test_input).getCmdLine();
        assertEquals(expected.getString(),actual.getString());
    }
}
