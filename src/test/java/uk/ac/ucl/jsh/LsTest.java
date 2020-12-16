package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Ls;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class LsTest {
    private static ArrayList<String> appArgs = new ArrayList<>();
    private static WorkingDr workingDir;
    private static String initWorkingDir;
    private static String dirPath = "/Users/coco/tmp"; //change when needed
    private static final Ls LS = new Ls();
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
        testFileHandle.createTestFileHierarchy(dirPath + "/Other/a/b/c", "test.out");
        testFileHandle.createTestFileHierarchy(dirPath + "/Test/d/e", "test1.out");
        testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test2.out");
        testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test3.txt");
        File.createTempFile(workingDir.getWD(), "test4.txt");
    }
    
    @Test
    public void testDotsPath() throws Exception {
        workingDir.setWD(dirPath + "/Other");
        appArgs.add("..");
        LS.exec(appArgs, System.in, out);
        String expectedOutput = "Other" + "\t" + "Test" + "\t" + "Documents" +"test4.txt" + System.getProperty("line.Separator");
        assertEquals(expectedOutput, out.toString());
    }


    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        File path = new File("/a/b/c");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
        workingDir.setWD(initWorkingDir);
    } 
}
