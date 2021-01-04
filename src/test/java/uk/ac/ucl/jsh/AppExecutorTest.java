package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.junit.Before;

import org.junit.Test;

import uk.ac.ucl.jsh.toolkit.AppExecutor;

public class AppExecutorTest {
    private static AppExecutor ae;
    private static ArrayList<String> args;
    private static ByteArrayOutputStream out;

    @Before
    public void SetClass() throws Exception {
        ae = AppExecutor.getInstance();
        args = new ArrayList<>();
        out = new ByteArrayOutputStream();
        args.add("foo");
    }

    @Test
    public void testUnsafe() throws Exception {
        ae.executeApp("_echo", args, null, out);
        assertEquals("foo" + System.getProperty("line.separator"), out.toString());
    }

}