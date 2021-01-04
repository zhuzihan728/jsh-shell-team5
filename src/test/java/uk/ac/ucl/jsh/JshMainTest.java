package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class JshMainTest {
    private static ByteArrayOutputStream outputStream;
    private static ByteArrayOutputStream errStream;
    private static ByteArrayInputStream inputStream;
    private static WorkingDr workingDir;
    private static String initWD;
    private static String[] args;
    private String lineSep = System.getProperty("line.separator");

    @BeforeClass
    public static void SetTest() {
        workingDir = WorkingDr.getInstance();
        initWD = workingDir.getWD();
    }

    @Before
    public void SetClass() throws Exception {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        errStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errStream));
        inputStream = new ByteArrayInputStream("hello".getBytes());
        System.setIn(inputStream);
    }

    @Test
    public void testWrongArgs() throws Exception {
        args = new String[3];
        JshMain.main(args);
        assertEquals("jsh: wrong number of arguments" + lineSep, outputStream.toString());
    }

    @Test
    public void testWrongFirstArg() throws Exception {
        args = new String[2];
        args[0] = "hi";
        JshMain.main(args);
        assertEquals("jsh: " + args[0] + ": unexpected argument" + lineSep, outputStream.toString());
    }

    @Test
    public void testRun() throws Exception {
        args = new String[2];
        args[0] = "-c";
        args[1] = "echo foo";
        JshMain.main(args);
        assertEquals("foo" + lineSep, outputStream.toString());
    }

    @Test
    public void testWrongRun() throws Exception {
        args = new String[2];
        args[0] = "-c";
        args[1] = "hi";
        JshMain.main(args);
        assertEquals("jsh: hi is not an application" + lineSep, errStream.toString());
    }

    @Test
    public void testWrongRun2() throws Exception {
        args = new String[2];
        args[0] = "-c";
        args[1] = "hi";
        JshMain.main(args);
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testRunOnTerminal() throws Exception {
        args = new String[0];
        JshMain.main(args);
        String out = outputStream.toString();
        assertEquals(initWD+"> ", out.substring(out.indexOf(">")+2));
    }

    @Test
    public void testErrOnTerminal() throws Exception {
        args = new String[0];
        JshMain.main(args);
        String out = errStream.toString();
        String[] errMsg = out.split(lineSep);
        assertEquals("jsh: hello is not an application", errMsg[0]);
    }

    @Test
    public void testErrOnTerminal2() throws Exception {
        args = new String[0];
        JshMain.main(args);
        String out = errStream.toString();
        String[] errMsg = out.split(lineSep);
        assertEquals("No line found", errMsg[1]);
    }

}