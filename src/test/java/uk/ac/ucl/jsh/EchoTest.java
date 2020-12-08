package uk.ac.ucl.jsh;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Echo;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EchoTest {
    private static ArrayList<String> appArgs = new ArrayList<>();
    private static WorkingDr workingDir;
    private static String initWorkingDir;
    private static final Echo ECHO = new Echo();
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void SetTest() {
        out = new ByteArrayOutputStream();
        workingDir = WorkingDr.getInstance();
        initWorkingDir = workingDir.getWD();
        workingDir.setWD("user.dir");
    }

    @Test
    public void testFreeSingle() throws Exception {
        appArgs.add("foo");
        ECHO.exec(appArgs, System.in, out);
        assertEquals("foo" + System.getProperty("line.separator"), out.toString());
    }

    @Test
    public void testFreeMulti() throws Exception {
        appArgs.clear();
        out.reset();
        appArgs.add("first");
        appArgs.add("second");
        appArgs.add("third");
        ECHO.exec(appArgs, System.in, out);
        assertEquals("first second third" + System.getProperty("line.separator"), out.toString());
    }

    @Test
    public void testNull() throws Exception {
        appArgs.clear();
        out.reset();
        ECHO.exec(appArgs, null, out);
        assertEquals("", out.toString().trim());
    }

    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        workingDir.setWD(initWorkingDir);
    }
}