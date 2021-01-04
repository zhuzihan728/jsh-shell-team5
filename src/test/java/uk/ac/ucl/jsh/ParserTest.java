package uk.ac.ucl.jsh;

import org.junit.*;
import uk.ac.ucl.jsh.call_parts.OutputRedirection;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Command;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;
import uk.ac.ucl.jsh.parser.CmdLineParser;
import uk.ac.ucl.jsh.parser.JshCaller;
import uk.ac.ucl.jsh.toolkit.InputReader;
import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ParserTest {
    private static String dirPath;
    private static WorkingDr workingDir;
    private static String initWD;
    private static ArrayList<Sub_Call> arguments;
    private static OutputRedirection or;

    @BeforeClass
    public static void SetTest() {
        workingDir = WorkingDr.getInstance();
        initWD = workingDir.getWD();
        dirPath = initWD + "/temp/Test";
        workingDir.setWD(dirPath);
    }

    @Before
    public void SetClass() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.createTestFileHierarchy(dirPath, "from.txt");
        testFileHandle.createTestFileHierarchy(dirPath, "b");
        testFileHandle.createTestFileHierarchy(dirPath, "c");
        FileWriter writer = new FileWriter(dirPath + "from.txt");
        writer.write("hi");
        writer.close();
    }

    private ArrayList<String> sub_call_to_string(ArrayList<Sub_Call> sub_calls) throws JshException {
        ArrayList<String> result = new ArrayList<>();
        for(Sub_Call sub_call : sub_calls){
            ArrayList<String> array = sub_call.get_OutputArray();
            if(array != null) {
                result.add(sub_call.getString());
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
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void testSingleQuoted() throws JshException {
        String test_input = "echo a'a b c `echo def`' ";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","aa b c `echo def`"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void testDoubleQuoted() throws JshException {
        String test_input = "echo \"hi\" \"how `echo are`\"";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","hi","how are"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void mixedQuotedTest() throws JshException {
        String test_input = "echo a\"hi\"b c\"how `echo are`\"d";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","ahib","chow ared"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void mixedQuotedTest2() throws JshException {
        String test_input = "echo a\"hi\"b \"how `echo are`\" cd ";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","ahib","how are", "cd"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void mixedQuotedTest3() throws JshException {
        String test_input = "echo a b c\"d\" e f g h ";
        ArrayList<String> expected_out =
                new ArrayList(Arrays.asList("echo","a","b","cd", "e", "f","g","h"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void testBackQuoted() throws JshException {
        String test_input = "echo `echo hello` ";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("echo","hello"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void testRedirection1() throws JshException {
        String test_input = "a > b";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void testRedirection2() throws JshException {
        String test_input = "a < b";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void testRedirection3() throws JshException {
        String test_input = "a < b > c";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
    }

    @Test
    public void testRedirection4() throws JshException {
        String test_input = "a > b > c";
        ArrayList<String> expected_out = new ArrayList(Arrays.asList("a"));
        CmdLineParser parser = new CmdLineParser(test_input);
        ArrayList<Sub_Call> parser_out = parser.getTokens();
        assertEquals(expected_out, sub_call_to_string(parser_out));
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

    @Test
    public void testmultiPipe(){
        String test_input = "a|b|c|d";
        Command expected =
                new Pipe(
                new Pipe(new Pipe(new Call("a"), new Call("b")), new Call("c")), new Call("d"));
        Command actual = new CmdLineParser(test_input).getCmdLine();
        assertEquals(expected.getString(),actual.getString());
    }

    @Test
    public void testmultiSeq(){
        String test_input = "a;b;c;d";
        Command expected =
                new Sequence(
                        new Sequence(new Sequence(new Call("a"), new Call("b")), new Call("c")), new Call("d"));
        Command actual = new CmdLineParser(test_input).getCmdLine();
        assertEquals(expected.getString(),actual.getString());
    }

    @Test
    public void testCallFunction() throws JshException {
        String test_input = "echo `echo hi`";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new Call(test_input).eval(new JshCaller(),null,out);
        assertEquals("hi",out.toString().trim());
    }

    @Test
    public void testCallFunction1() throws JshException, IOException {
        String test_input = "echo `echo hi` > to.txt";
        new Call(test_input).eval(new JshCaller(),null,null);
        String out = InputReader.fileContent_List("to.txt").get(0);
        assertEquals("hi", out);
    }

    @Test
    public void testCallFunction2() throws JshException, IOException {
        new Call("echo hi > from.txt").eval(new JshCaller(),null,null);
        String test_input = "cat < from.txt";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new Call(test_input).eval(new JshCaller(),System.in,out);
        assertEquals("hi", out.toString().trim());
    }

    @After
    // Delete the test hierarchy
    public void afterTest() throws IOException {
        File path = new File(dirPath);
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
        // workingDir.setWD(dirPath);
    }

    @AfterClass
    public static void EndTest() throws IOException {
        workingDir.setWD(initWD);
        File path = new File(initWD + "/temp");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
    }
}
