package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.call_parts.BaseCall;
import uk.ac.ucl.jsh.call_parts.OutputRedirection;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class OutputRedirectionTest {
    private static String dirPath;
    private static WorkingDr workingDir;
    private static String initWD;
    private static ArrayList<Sub_Call> arguments;
    private static OutputRedirection or;

    @BeforeClass
    public static void SetTest() {
        workingDir = WorkingDr.getInstance();
        initWD = workingDir.getWD();
        dirPath = initWD + "/tmp/Test";
        workingDir.setWD(dirPath);
    }

    @Before
    public void SetClass() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.createTestFileHierarchy(dirPath, "test.txt");
        arguments = new ArrayList<>();
        BaseCall a = new BaseCall("test.txt", 0);
        BaseCall b = new BaseCall("hello", 0);
        arguments.add(a);
        arguments.add(b);
        or = new OutputRedirection(arguments);
    }

    @Test
    public void testGetString() throws Exception {
        assertEquals(null, or.getString());
    }

    @Test
    public void testGetType() throws Exception {
        String a = "OutputRedirection";
        assertEquals(a, or.getType());
    }

    @Test
    public void testGetIn() throws Exception {
        assertEquals(null, or.getInput());
    }

    @Test
    public void testGetOut() throws Exception {
        assertNotNull(or.getOutput());
    }

    @Test
    public void testGetArray() throws Exception {
        assertEquals(null, or.get_OutputArray());
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
        File path = new File(initWD + "/tmp");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
    }
}