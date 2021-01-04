package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.call_parts.BaseCall;
import uk.ac.ucl.jsh.call_parts.InputRedirection;
import uk.ac.ucl.jsh.call_parts.Sub_Call;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class InputRedirectionTest {
    private static String call;
    private static int type;
    private static String dirPath;
    private static WorkingDr workingDir;
    private static String initWD;
    private static ArrayList<Sub_Call> arguments = new ArrayList<>();
    private static InputRedirection ir;


    @BeforeClass
    public static void SetTest() {
        workingDir = WorkingDr.getInstance();
        initWD = workingDir.getWD();
        dirPath = initWD + "/tmp/Test";
        workingDir.setWD(dirPath);
    }
    public static void setClass() {
        BaseCall a = new BaseCall("test.txt", 0);
        BaseCall b = new BaseCall("hello", 0);
        arguments.add(a);
        arguments.add(b);
        ir = new InputRedirection(arguments);
    }

    @Before
    public void createDir() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.createTestFileHierarchy(dirPath, "test.txt");
        new File(dirPath + "/test1.txt").createNewFile();

    }

   

    
    @Test
    public void testGetString() throws Exception {
        assertEquals(null, ir.getString());
    }

    @Test
    public void testGetType() throws Exception {
        call = "*";
        type = 1;
        BaseCall bc = new BaseCall(call, type);
        String a = "BaseCall";
        assertEquals(a, bc.getType());
    }

    @Test
    public void testGetIn() throws Exception {
        call = "*";
        type = 1;
        BaseCall bc = new BaseCall(call, type);
        assertEquals(null, bc.getInput());
    }

    @Test
    public void testGetOut() throws Exception {
        call = "*";
        type = 1;
        BaseCall bc = new BaseCall(call, type);
        assertEquals(null, ir.getOutput());
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