package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Cd;
import uk.ac.ucl.jsh.applications.Unsafe;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class UnsafeTest {
    private static ByteArrayOutputStream out;
    private static ArrayList<String> appArgs;
    private Unsafe _cd;
    private static String dirPath;
    private static WorkingDr workingDir;

    
    @BeforeClass
    public static void SetTest() {
        appArgs = new ArrayList<>();
        out = new ByteArrayOutputStream();
        workingDir = WorkingDr.getInstance();
        dirPath = workingDir.getWD();
    }

    @Before
    public void createDir() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.createTestFileHierarchy(dirPath + "/Other/a/b/c", "test.out");
    }

    @Before
    public void UnsafeApps() {
        this._cd = new Unsafe(new Cd());
    }

    @Test
    public void testPath() throws Exception {
        workingDir.setWD(dirPath + "/Other/a/b/c");
        appArgs.add("..");
        _cd.exec(appArgs, System.in, out);
        assertEquals(dirPath + "/Other/a/b", workingDir.getWD());
    }

    @After
    // Delete the test hierarchy, reset the command arguments and reset the
    // outputstream
    public void afterTest() throws IOException {
        out.reset();
        appArgs.clear();
        File path = new File(dirPath + "/Other");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
        workingDir.setWD(dirPath);
    }
}