package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Cd;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class CdTest {
    private static ArrayList<String> appArgs = new ArrayList<>();
    private static WorkingDr workingDir;
    private static String initWorkingDir;
    private static String dirPath = "/Users/coco/temp"; //change when needed
    private static final Cd CD = new Cd();
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void SetTest() {
        out = new ByteArrayOutputStream();
        workingDir = WorkingDr.getInstance();
        initWorkingDir = workingDir.getWD();
        workingDir.setWD(dirPath);
    }

    @Before
    public void createDir() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.createTestFileHierarchy(dirPath + "/a/b/c", "test.out");
    }

    @Test
    public void testPath() throws Exception {
        workingDir.setWD("/Users/coco/temp/a/b/c");
        appArgs.add("..");
        CD.exec(appArgs, System.in, out);
        assertEquals(dirPath + "/a/b", workingDir.getWD());
    }

    // test dot, test path 不支持三个点 或者invalid

    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        File path = new File("/a/b/c");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
        workingDir.setWD(initWorkingDir);
    }
}
