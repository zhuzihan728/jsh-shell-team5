package uk.ac.ucl.jsh;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.parser.CmdLineParser;
import uk.ac.ucl.jsh.toolkit.JshException;

import java.util.ArrayList;
import java.util.Arrays;

public class ParserTest {

    private ArrayList<String> sub_call_to_string(ArrayList<Sub_Call> sub_calls) throws JshException {
        ArrayList<String> result = new ArrayList<>();
        for(Sub_Call sub_call : sub_calls){
            result.addAll(sub_call.get_OutputArray());
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



}
