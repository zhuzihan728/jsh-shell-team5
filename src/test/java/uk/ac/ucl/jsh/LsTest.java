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

import uk.ac.ucl.jsh.applications.Ls;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class LsTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Ls LS = new Ls();
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
            testFileHandle.createTestFileHierarchy(dirPath + "/Other/a/b/c", "test.out");
        } catch (Exception e) {
        }
        try {
            new File(dirPath + "/Other/.test").createNewFile();
        } catch (Exception e) {
        }
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Try/d", "test1.out");
        } catch (Exception e) {
        }
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Try/e", "test2.txt");
        } catch (Exception e) {
        }
        try {
            new File(dirPath + "/Try/test3.txt").createNewFile();
        } catch (Exception e) {
        }
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test4.out");
        } catch (Exception e) {
        }
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents/f", "test5.txt");
        } catch (Exception e) {
        }
        try {
            new File(dirPath + "/Documents/test6.txt").createNewFile();
        } catch (Exception e) {
        }
        try {
            new File(dirPath + "/test7.txt").createNewFile();
        } catch (Exception e) {
        }
    }

    public String cre(String path) {
        File file = new File(path);
        try {
            file.mkdirs();
        } catch (Exception e) {
        }
        try {
            file.createNewFile();
        } catch (Exception e) {
        }
        return path;
    }
    /*
     * file.separator: / line.separator: \n
     */

    @Test
    public void testDotsPath() throws Exception {
        workingDir.setWD(
                cre(dirPath + System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator")));
        appArgs.add("..");
        LS.exec(appArgs, System.in, out);
        String expectedOutput = "test7.txt" + "\t" + "Other" + "\t" + "Try" + "\t" + "Documents" + "\t" + "tmp" + "\t"
                + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testCurrentDirectory() throws Exception {
        // working Directory under Try
        workingDir.setWD(
                cre(dirPath + System.getProperty("file.separator") + System.getProperty("file.separator") + "Try"));
        // appArgs.add("");
        LS.exec(appArgs, System.in, out);
        String expectedOutput = "test3.txt" + "\t" + "d" + "\t" + "e" + "\t" + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testArguDirectoryRelative() throws Exception {
        workingDir.setWD(cre(dirPath));
        appArgs.add("Documents");
        LS.exec(appArgs, System.in, out);
        String expectedOutput = "test6.txt" + "\t" + "test4.out" + "\t" + "f" + "\t"
                + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testArguDirectoryAbsolute() throws Exception {
        cre(dirPath + System.getProperty("file.separator") + "Documents");
        appArgs.add(System.getProperty("file.separator") + "Documents");
        LS.exec(appArgs, System.in, out);
        String expectedOutput = "test6.txt" + "\t" + "test4.out" + "\t" + "f" + "\t"
                + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testDotFile() throws Exception {
        cre(dirPath + System.getProperty("file.separator") + "Other");
        appArgs.add(System.getProperty("file.separator") + "Other");
        LS.exec(appArgs, System.in, out);
        String expectedOutput = "a" + "\t" + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    // @Test
    // public void testGlobbing() throws Exception {
    // appArgs.add(System.getProperty("file.separator") + "tmp" +
    // System.getProperty("file.separator") + "Test"
    // + System.getProperty("file.separator") + "D*s");
    // LS.exec(appArgs, System.in, out);
    // String expectedOutput = "test6.txt" + "\t" + "test4.out" + "\t" + "f" + "\t"
    // + System.getProperty("line.separator");
    // assertEquals(expectedOutput, out.toString());
    // }

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

    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        workingDir.setWD(initWD);
        File path = new File(initWD + "/tmp");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
    }
}