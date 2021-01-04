package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.*;

import uk.ac.ucl.jsh.applications.Head;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;


public class HeadTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Head HEAD = new Head();
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
        File file = new File(
                Paths.get(dirPath + System.getProperty("file.separator") + "Documents", "test8.txt").toString());
        if (!file.exists()) {
            file.createNewFile();
        }
        CatTest.cre(Paths.get(dirPath + System.getProperty("file.separator") + "Documents", "test8.txt").toString());
        testFileHandle.createTestFileHierarchy(dirPath + "/Other", "test.out");

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
        HEAD.exec(appArgs, testInputStream, out);
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
        HEAD.exec(appArgs, testInputStream, out);
        assertEquals("Hello world" + System.getProperty("line.separator"), out.toString());
    }

    @Test
    public void testTooManyArgs() throws Exception {
        try {
            appArgs.add("one");
            appArgs.add("two");
            appArgs.add("three");
            appArgs.add("four");
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("head: too many arguments", e.getMessage());
        }
    }

    @Test
    public void testNegativeLines() throws Exception {
        try {
            appArgs.add("-n");
            appArgs.add("-1");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents"
                    + System.getProperty("file.separator") + "test4.txt");
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("head: " + dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "illegal line count -- -1", e.getMessage());
        }
    }

    @Test
    public void testMissingInput() throws Exception {
        try {
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("head: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testOnlyTwoInputArgs() throws Exception {
        try {
            appArgs.add("-n");
            appArgs.add("10");
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("head: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testArgsMissingDashN() throws Exception {
        try {
            appArgs.add("10");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("head: wrong argument " + "10", e.getMessage());
        }
    }

    @Test
    public void testSecondArgNotNumber() throws Exception {
        try {
            appArgs.add("-n");
            appArgs.add("Not Number");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("head: wrong argument Not Number", e.getMessage());
        }
    }

    @Test
    public void testInvalidFilePath() {
        try {
            appArgs.add("-n");
            appArgs.add("10");
            appArgs.add("Random");
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("head: " + "Random does not exist", e.getMessage());
        }
    }

    @Test
    public void testNotFilePath() {
        try {
            appArgs.add("-n");
            appArgs.add("10");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Other");
            HEAD.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals(
                    "head: " + dirPath + System.getProperty("file.separator") + "Other is a directory",
                    e.getMessage());
        }
    }

    @Test
    public void testDefaultLines() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 10; ++i) {
            expectedOutput += "Line" + Integer.toString(i) + System.getProperty("line.separator");
        }
        HEAD.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testCustomLines() throws Exception {
        appArgs.add("-n");
        appArgs.add("20");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        String expectedOutput = new String();
        for (int i = 0; i < 20; ++i) {
            expectedOutput += "Line" + Integer.toString(i) + System.getProperty("line.separator");
        }
        HEAD.exec(appArgs, null, out);
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
        HEAD.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testEmptyFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test8.txt");
        HEAD.exec(appArgs, null, out);
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
        workingDir.setWD(initWD);
        File path = new File(initWD + "/tmp");
        TestFileHandle testFileHandle = new TestFileHandle();
        testFileHandle.deleteFileHierarchy(path);
    }
}
