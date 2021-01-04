package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Cd;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class CdTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Cd CD = new Cd();
    private static ByteArrayOutputStream out;

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

    @Test
    public void testPath() throws Exception {
        workingDir.setWD(dirPath + "/Other/a/b/c");
        appArgs.add("..");
        CD.exec(appArgs, System.in, out);
        assertEquals(dirPath + "/Other/a/b", workingDir.getWD());
    }

    @Test
    public void testMissingArgs() throws Exception {
        try {
            CD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            assertEquals("cd: missing argument", e.getMessage());
        }
    }

    @Test
    public void testTooManyArgs() throws Exception {
        try {
            appArgs.add("..");
            appArgs.add("..");
            appArgs.add("..");
            CD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            assertEquals("cd: too many arguments", e.getMessage());
        }
    }

    @Test
    public void testInvaildPath() throws Exception {
        try {
            appArgs.add("random" + System.getProperty("file.separator") + "Path");// find a random directory that can't
                                                                                  // be find
            CD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            String dirExpectMessage = "cd: " + "random" + System.getProperty("file.separator") + "Path"
                    + "is not an existing directory";
            if (null != e.getMessage()) {
                assertTrue(dirExpectMessage, e.getMessage().contains("is not an existing directory"));
            }
        }
    }

    @Test
    public void testFilePath() throws Exception {
        try {
            appArgs.add(System.getProperty("file.separator") + "Testfile");
            CD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            String fileExpectMessage = "cd: " + System.getProperty("file.separator") + "Testfile"
                    + "is not an existing directory";
            if (null != e.getMessage()) {
                assertTrue(fileExpectMessage, e.getMessage().contains("is not an existing directory"));
            }
        }
    }

    @Test
    public void testDotsPath() throws Exception {
        try {
            appArgs.add("...");
            CD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            String dotsExpectMessage = "cd: " + "..." + "is not an existing directory";
            if (null != e.getMessage()) {
                assertTrue(dotsExpectMessage, e.getMessage().contains("is not an existing directory"));
            }
        }
    }

    @Test
    public void testCurrentPath() throws Exception {
        appArgs.add(".");
        CD.exec(appArgs, System.in, out);
        assertEquals(dirPath, workingDir.getWD());
    }

    @Test
    public void testExistPath() throws Exception {
        appArgs.add("Other" + System.getProperty("file.separator") + "a");
        CD.exec(appArgs, System.in, out);
        assertEquals(
                dirPath + System.getProperty("file.separator") + "Other" + System.getProperty("file.separator") + "a",
                workingDir.getWD());
    }

    @Test
    public void testAbsolutePath() throws Exception {
        try {
            appArgs.add(dirPath + "Other" + System.getProperty("file.separator") + "a");
            CD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            String dotsExpectMessage = "cd: " + dirPath + "Other" + System.getProperty("file.separator") + "a"
                    + "is not an existing directory";
            if (null != e.getMessage()) {
                assertTrue(dotsExpectMessage, e.getMessage().contains("is not an existing directory"));
            }
        }
    }

    @Test
    public void testRootPath() throws Exception {
        appArgs.add("..");
        workingDir.setWD(System.getProperty("file.separator"));
        CD.exec(appArgs, System.in, out);
        assertEquals(System.getProperty("file.separator"), workingDir.getWD());
    }

    @Test
    public void testGlobbing() throws Exception {
        try {
            appArgs.add("");
            CD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals("cd: too many arguments", e.getMessage());
        }
    }


    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        workingDir.setWD(dirPath);
    }
}