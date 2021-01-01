package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.*;

import org.junit.runners.MethodSorters;
import uk.ac.ucl.jsh.applications.Tail;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;


public class TailTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Tail TAIL = new Tail();
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void SetTest() {
        appArgs = new ArrayList<>();
        out = new ByteArrayOutputStream();
        workingDir = WorkingDr.getInstance();
        dirPath = workingDir.getWD() + "/tmp/Test";
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
    public void testInputStream() throws Exception {
        ByteArrayOutputStream bwi = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bwi));
        String expectedOutput = "Hello world" + System.getProperty("line.separator") + "From Input Stream"
                + System.getProperty("line.separator");
        writer.write(expectedOutput);
        writer.flush();
        writer.close();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(bwi.toByteArray());
        TAIL.exec(appArgs, testInputStream, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testInputStreamCustomLines() throws Exception {
        ByteArrayOutputStream bwi = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bwi));
        String expectedOutput = "Hello world" + System.getProperty("line.separator") + "From Input Stream"
                + System.getProperty("line.separator");
        writer.write(expectedOutput);
        writer.flush();
        writer.close();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(bwi.toByteArray());
        appArgs.add("-n");
        appArgs.add("1");
        TAIL.exec(appArgs, testInputStream, out);
        assertEquals("From Input Stream" + System.getProperty("line.separator"), out.toString());
    }

    @Test
    public void testTooManyArgs() throws Exception {
        try {
            appArgs.add("one");
            appArgs.add("two");
            appArgs.add("three");
            appArgs.add("four");
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: too many arguments", e.getMessage());
        }
    }

    @Test
    public void testNegativeLines() throws Exception {
        try {
            appArgs.add("-n");
            appArgs.add("-1");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents"+ System.getProperty("file.separator") + "test4.txt");
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: " + dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "illegal line count -- -1", e.getMessage());
        }
    }

    @Test
    public void testMissingInput() throws Exception {
        try {
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testOnlyTwoInputArgs() throws Exception {
        try {
            appArgs.add("-n");
            appArgs.add("10");
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testArgsMissingDashN() throws Exception {
        try {
            appArgs.add("10");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: wrong argument " + "10", e.getMessage());
        }
    }

    @Test
    public void testSecondArgNotNumber() throws Exception {
        try {
            appArgs.add("-n");
            appArgs.add("Not Number");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: wrong argument Not Number", e.getMessage());
        }
    }

    @Test
    public void testInvalidFilePath() {
        try {
            appArgs.add("-n");
            appArgs.add("10");
            appArgs.add("Random");
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: " + "Random does not exist", e.getMessage());
        }
    }

    @Test
    public void testNotFilePath() {
        try {
            appArgs.add("-n");
            appArgs.add("10");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Other");
            TAIL.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("tail: " + dirPath + System.getProperty("file.separator") + "Other is a directory",
                    e.getMessage());
        }
    }

    @Test
    public void testDefaultLines() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        String expectedOutput = new String();
        for (int i = 20; i < 30; ++i) {
            expectedOutput += "Line" + Integer.toString(i) + System.getProperty("line.separator");
        }
        TAIL.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testCustomLines() throws Exception {
        appArgs.add("-n");
        appArgs.add("20");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        String expectedOutput = new String();
        for (int i = 10; i < 30; ++i) {
            expectedOutput += "Line" + Integer.toString(i) + System.getProperty("line.separator");
        }
        TAIL.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testCustomOverLines() throws Exception {
        appArgs.add("-n");
        appArgs.add("80");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += "Line" + Integer.toString(i) + System.getProperty("line.separator");
        }
        TAIL.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testEmptyFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test8.txt");
        TAIL.exec(appArgs, null, out);
        assertEquals("", out.toString());
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

    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        workingDir.setWD(dirPath);
    }

    // testGlobbedPathFile

}
