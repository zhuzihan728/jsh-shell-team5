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
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class BaseCallTest {
    private static String call;
    private static int type;
    private static String dirPath;
    private static WorkingDr workingDir;
    private static String initWD;

    @BeforeClass
    public static void SetTest() {
        workingDir = WorkingDr.getInstance();
        initWD = workingDir.getWD();
        dirPath = initWD + "/tmp/Test";
        workingDir.setWD(dirPath);
    }

    @Before
    public void createDir() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.createTestFileHierarchy(dirPath, "test.txt");
        new File(dirPath + "/test1.txt").createNewFile();

    }

    @Test
    public void testNonQuoted() throws Exception {
        call = "*";
        type = 1;
        BaseCall bc = new BaseCall(call, type);
        ArrayList<String> a = new ArrayList<>();
        a.add("test1.txt");
        a.add("test.txt");
        assertEquals(a, bc.get_OutputArray());
    }

    @Test
    public void testSingle() throws Exception {
        call = "'hello'";
        type = 2;
        BaseCall bc = new BaseCall(call, type);
        ArrayList<String> a = new ArrayList<>();
        a.add("hello");
        assertEquals(a, bc.get_OutputArray());
    }

    @Test
    public void testElseType() throws Exception {
        call = "hello";
        type = 0;
        BaseCall bc = new BaseCall(call, type);
        ArrayList<String> a = new ArrayList<>();
        a.add(call);
        assertEquals(a, bc.get_OutputArray());
    }

    @Test
    public void testGetString() throws Exception {
        call = "*";
        type = 1;
        BaseCall bc = new BaseCall(call, type);
        String a = "test1.txt test.txt";
        assertEquals(a, bc.getString());
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
        assertEquals(null, bc.getOutput());
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