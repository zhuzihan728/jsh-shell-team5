package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Cut;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class CutTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Cut CUT = new Cut();
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
    public void createFile() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test4.txt");
        } catch (Exception e) {
        }
        CatTest.cre(Paths.get(dirPath + System.getProperty("file.separator") + "/Documents", "test8.txt").toString());
        CatTest.cre(Paths.get(dirPath + "/Other", "test.out").toString());

        CatTest.cre(dirPath + "/Documents/test4.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(dirPath + "/Documents/test4.txt"));
        bw.write(testFileHandle.generateLongFileText(30));
        bw.close();
    }

    @Test
    public void testTooManyArgs() throws Exception {
        try {
            appArgs.add("one");
            appArgs.add("two");
            appArgs.add("three");
            appArgs.add("four");
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: too many arguments", e.getMessage());
        }
    }

    @Test
    public void testMissingArgs() throws Exception {
        try {
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: missing arguments", e.getMessage());
        }
    }

    @Test
    public void testOnlyTwoInputArgs() throws Exception {
        try {
            appArgs.add("-b");
            appArgs.add("10");
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testArgsMissingDashB() throws Exception {
        try {
            appArgs.add("10");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: wrong argument " + "10", e.getMessage());
        }
    }

    @Test
    public void testSecondArgNotMatch() throws Exception {
        try {
            appArgs.add("-b");
            appArgs.add("Not Match");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: wrong argument " + "Not Match", e.getMessage());
        }
    }

    @Test
    public void testInvalidArgument() {
        try {
            appArgs.add("-b");
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: missing arguments", e.getMessage());
        }
    }

    @Test
    public void testInvalidFilePath() {
        try {
            appArgs.add("-b");
            appArgs.add("10");
            appArgs.add("Random");
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: " + "Random does not exist", e.getMessage());
        }
    }

    @Test
    public void testNotFilePath() {
        try {
            appArgs.add("-b");
            appArgs.add("10");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Other");
            CUT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("cut: " + dirPath + System.getProperty("file.separator") + "Other is a directory", e.getMessage());
        }
    }

    @Test
    public void testEmptyFile() throws Exception {
        appArgs.add("-b");
        appArgs.add("10");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test8.txt");
        CUT.exec(appArgs, null, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testSingleEachLine() throws Exception {
        appArgs.add("-b");
        appArgs.add("1");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += "L" + System.getProperty("line.separator");
        }
        CUT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testDoubleEachLine() throws Exception {
        appArgs.add("-b");
        appArgs.add("1,2");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += "Li" + System.getProperty("line.separator");
        }
        CUT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testMultiEachLine() throws Exception {
        appArgs.add("-b");
        appArgs.add("1,2,4");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += "L" + "i" + "e" +System.getProperty("line.separator");
        }
        CUT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testRangeByte() throws Exception {
        appArgs.add("-b");
        appArgs.add("1-3");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += "L" + "i" + "n" + System.getProperty("line.separator");
        }
        CUT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testDashNumber() throws Exception {
        appArgs.add("-b");
        appArgs.add("-3");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += "L" + "i" + "n" + System.getProperty("line.separator");
        }
        CUT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }
   
    @Test
    public void testNumberDash() throws Exception {
        appArgs.add("-b");
        appArgs.add("5-");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += Integer.toString(i) + System.getProperty("line.separator");
        }
        CUT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    } 

    @After
    // Delete the test hierarchy, reset the command arguments and reset the outputstream
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
