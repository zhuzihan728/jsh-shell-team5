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

import uk.ac.ucl.jsh.toolkit.Globbing;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class GlobbingTest {
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
    public void testNoMatch() throws Exception {
        Globbing gl = new Globbing("*.py");
        ArrayList<String> a = new ArrayList<>();
        a.add("*.py");
        assertEquals(a, gl.getGlobbed_results());

    }

    @Test
    public void testIoErr() throws Exception {
        try {
            new Globbing("a/b/v/c/s");
        } catch (RuntimeException e) {
            assertEquals(dirPath + System.getProperty("file.separator") + "a/b/v/c", e.getMessage());
        }
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
