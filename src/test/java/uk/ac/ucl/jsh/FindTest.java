package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Find;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class FindTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Find FIND = new Find();
    private static ByteArrayOutputStream out;
    private static String initWD;

    @BeforeClass
    public static void SetTest() {
        appArgs = new ArrayList<>();
        out = new ByteArrayOutputStream();
        workingDir = WorkingDr.getInstance();
        initWD = workingDir.getWD();
        dirPath = initWD + "/tmp/Test";
    }

    @Before
    public void createDir() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Other", "test.out");
        } catch (Exception e) {
        }
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test4.out");
        } catch (Exception e) {
        }
        try {
            new File(dirPath + "/test6.txt").createNewFile();
        } catch (Exception e) {
        }
        try {
            new File(dirPath + "/Documents/test2.txt").createNewFile();
        } catch (Exception e) {
        }
        try {
            new File(dirPath + "/Documents/test3.txt").createNewFile();
        } catch (Exception e) {
        }
        try {
            new File(workingDir.getWD() + "/Documents").mkdirs();
        } catch (Exception e) {
        }
    }

    @Test
    public void testDirectoryFromCurrentDirectory() throws Exception {
        appArgs.add("-name");
        appArgs.add("Other");
        FIND.exec(appArgs, null, out);
        assertEquals("." + "/tmp/Test" + System.getProperty("file.separator") + "Other"
                + System.getProperty("line.separator") + System.getProperty("line.separator"), out.toString());
    }

    @Test
    public void testFileFromCurrentDirectory() throws Exception {
        appArgs.add("-name");
        appArgs.add("test6.txt");
        FIND.exec(appArgs, null, out);
        assertEquals("." + "/tmp/Test" + System.getProperty("file.separator") + "test6.txt"
                + System.getProperty("line.separator") + System.getProperty("line.separator"), out.toString());
    }

    @Test
    public void testMultipleFiles() throws Exception {
        appArgs.add("/tmp/Test/Documents");
        appArgs.add("-name");
        appArgs.add("test*");
        FIND.exec(appArgs, null, out);
        String expectedOutput = System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator")
                + "Test" + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.out" + System.getProperty("line.separator") + System.getProperty("file.separator") + "tmp"
                + System.getProperty("file.separator") + "Test" + System.getProperty("file.separator") + "Documents"
                + System.getProperty("file.separator") + "test2.txt" + System.getProperty("line.separator")
                + System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator") + "Test"
                + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test3.txt" + System.getProperty("line.separator") + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testMissingArgu() throws Exception {
        try {
            FIND.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("find: missing arguments", e.getMessage());
        }
    }

    @Test
    public void testMissingPattern() {
        try {
            appArgs.add(System.getProperty("file.separator") + "tmp");
            appArgs.add("-name");
            FIND.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("find: wrong argument " + System.getProperty("file.separator") + "tmp", e.getMessage());
        }
    }

    @Test
    public void testPatterMatching() throws Exception {
        appArgs.add("/tmp/Test/Documents");
        appArgs.add("-name");
        appArgs.add("*t*");
        FIND.exec(appArgs, null, out);
        String expectedOutput = System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator")
                + "Test" + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.out" + System.getProperty("line.separator") + System.getProperty("file.separator") + "tmp"
                + System.getProperty("file.separator") + "Test" + System.getProperty("file.separator") + "Documents"
                + System.getProperty("file.separator") + "test2.txt" + System.getProperty("line.separator")
                + System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator") + "Test"
                + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test3.txt" + System.getProperty("line.separator") + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }


    @Test
    public void testTooManyArgs() throws Exception {
        try {
            appArgs.add("one");
            appArgs.add("two");
            appArgs.add("three");
            appArgs.add("four");
            FIND.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("find: too many arguments", e.getMessage());
        }
    }

    @Test
    public void testInvailPath() throws Exception {
        try {
            appArgs.add("RandomPath");
            appArgs.add("-name");
            appArgs.add("Random");
            FIND.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("find: could not open RandomPath", e.getMessage());
        }
    }

    @Test
    public void testNotFilePath() {
        try {
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            appArgs.add("-name");
            appArgs.add("Random");
            FIND.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("find: could not open " + dirPath + System.getProperty("file.separator") + "Documents",
                    e.getMessage());
        }
    }

    @Test
    public void testMissingDashName() {
        try {
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            appArgs.add("MissingName");
            appArgs.add("Random");
            FIND.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("find: wrong argument MissingName", e.getMessage());
        }
    }

    @Test
    public void testNoMatchingFile() throws Exception {
        try{
            appArgs.add("-name");
            appArgs.add("NotMatch");
            FIND.exec(appArgs, null, out);
        }
        catch (Exception e) {
            assertEquals("find: no such file or directory", e.getMessage());
        } 
    }



    
    // testFindFileFromAbsolutePath testFindFileFromLowerLeverInTree
    // testFindFileFromGlobbedPath testGlobbedPathAndPattern

    @After
    // Delete the test hierarchy, reset the command arguments and reset the
    // outputstream
    public void afterTest() throws IOException {
        out.reset();
        appArgs.clear();
        File path = new File(dirPath);
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
        // workingDir.setWD(dirPath);
    }

    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        workingDir.setWD(initWD);
        File path = new File(initWD + "/tmp");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
    }
}
