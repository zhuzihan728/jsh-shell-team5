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

import uk.ac.ucl.jsh.applications.Uniq;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class UniqTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Uniq UNIQ = new Uniq();
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
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test10.txt");
        } catch (Exception e) {
        }
        CatTest.cre(Paths.get(dirPath + System.getProperty("file.separator") + "/Documents", "test8.txt").toString());
        CatTest.cre(Paths.get(dirPath + "/Other", "test.out").toString());

        CatTest.cre(dirPath + "/Documents/test10.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(dirPath + "/Documents/test10.txt"));
        String testString = "hello" + System.getProperty("line.separator") + "hello" + System.getProperty("line.separator")
                            + "world" + System.getProperty("line.separator") + "word" + System.getProperty("line.separator")
                            + "2" + System.getProperty("line.separator") + "HellO" + System.getProperty("line.separator")
                            + "hello" + System.getProperty("line.separator") + "Hello" + System.getProperty("line.separator")
                            + "123" + System.getProperty("line.separator") + "123" + System.getProperty("line.separator")
                            + "anOTHer" + System.getProperty("line.separator") + "another" + System.getProperty("line.separator");
        bw.write(testString);
        bw.close();
    }

    @Test
    public void testTooManyArgs() throws Exception {
        try {
            appArgs.add("one");
            appArgs.add("two");
            appArgs.add("three");
            UNIQ.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("uniq: too many arguments", e.getMessage());
        }
    }

    @Test
    public void testMissingInput() throws Exception {
        try {
            UNIQ.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("uniq: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testArgsNotDashI() throws Exception {
        try {
            appArgs.add("-n");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            UNIQ.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("uniq: wrong argument" + "-n", e.getMessage());
        }
    }

    @Test
    public void testInvalidFilePath() {
        try {
            appArgs.add("Random");
            UNIQ.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("uniq: " + "Random does not exist", e.getMessage());
        }
    }

    @Test
    public void testNotFilePath() {
        try {
            appArgs.add(dirPath + System.getProperty("file.separator") + "Other");
            UNIQ.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("uniq: " + dirPath + System.getProperty("file.separator") + "Other is a directory", e.getMessage());
        }
    }

    @Test
    public void testEmptyFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test8.txt");
        UNIQ.exec(appArgs, null, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testInputStreamWithDashI() throws Exception {
        ByteArrayOutputStream bwi = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bwi));
        String expectedInput = "hello" + System.getProperty("line.separator") + "hello" + System.getProperty("line.separator")
                            + "HellO" + System.getProperty("line.separator") + "word" + System.getProperty("line.separator");
        writer.write(expectedInput);
        writer.flush();
        writer.close();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(bwi.toByteArray());
        appArgs.add("-i");
        UNIQ.exec(appArgs, testInputStream, out);
        assertEquals("hello" + System.getProperty("line.separator") + "word" + System.getProperty("line.separator"), out.toString());
    }

    @Test
    public void testInputStream() throws Exception {
        ByteArrayOutputStream bwi = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bwi));
        String expectedInput = "hello" + System.getProperty("line.separator") + "hello"
                + System.getProperty("line.separator") + "HellO" + System.getProperty("line.separator") + "word"
                + System.getProperty("line.separator");
        writer.write(expectedInput);
        writer.flush();
        writer.close();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(bwi.toByteArray());
        UNIQ.exec(appArgs, testInputStream, out);
        assertEquals("hello" + System.getProperty("line.separator") + "HellO" + System.getProperty("line.separator") + "word" + System.getProperty("line.separator"),
                out.toString());
    }

    @Test
    public void testFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test10.txt");
        String expectedOutput = "hello" + System.getProperty("line.separator") + "world" + System.getProperty("line.separator") + "word"
                + System.getProperty("line.separator") + "2" + System.getProperty("line.separator") + "HellO"
                + System.getProperty("line.separator") + "hello" + System.getProperty("line.separator") + "Hello"
                + System.getProperty("line.separator") + "123" + System.getProperty("line.separator") + "anOTHer" + System.getProperty("line.separator") + "another"
                + System.getProperty("line.separator");
        UNIQ.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testFileWithDashI() throws Exception {
        appArgs.add("-i");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test10.txt");
        String expectedOutput = "hello" + System.getProperty("line.separator") + "world"
                + System.getProperty("line.separator") + "word" + System.getProperty("line.separator") + "2"
                + System.getProperty("line.separator") + "HellO" + System.getProperty("line.separator") + "123" + System.getProperty("line.separator") + "anOTHer"
                + System.getProperty("line.separator");
        UNIQ.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
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

