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

import uk.ac.ucl.jsh.applications.Sort;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class SortTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Sort SORT = new Sort();
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
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "simple.txt");
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "mix.txt");
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "number.txt");
        } catch (Exception e) {
        }
        CatTest.cre(Paths.get(dirPath + System.getProperty("file.separator") + "/Documents", "test8.txt").toString());
        CatTest.cre(Paths.get(dirPath + "/Other", "test.out").toString());

        CatTest.cre(dirPath + "/Documents/simple.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(dirPath + "/Documents/simple.txt"));
        String testString = "abhishek" + System.getProperty("line.separator") + "chitransh"
                + System.getProperty("line.separator") + "satish" + System.getProperty("line.separator") + "rajan"
                + System.getProperty("line.separator") + "naveen" + System.getProperty("line.separator") + "divyam"
                + System.getProperty("line.separator") + "harsh" + System.getProperty("line.separator");
        bw.write(testString);
        bw.close();

        CatTest.cre(dirPath + "/Documents/mix.txt");
        BufferedWriter bwi = new BufferedWriter(new FileWriter(dirPath + "/Documents/mix.txt"));
        String testStringMix = "abc" + System.getProperty("line.separator") + "apple"
                + System.getProperty("line.separator") + "7" + System.getProperty("line.separator")+ "BALL" + System.getProperty("line.separator") + "Abc"
                + System.getProperty("line.separator") + "1" + System.getProperty("line.separator") + "bat" + System.getProperty("line.separator");
        bwi.write(testStringMix);
        bwi.close();
    
        CatTest.cre(dirPath + "/Documents/number.txt");
        BufferedWriter w = new BufferedWriter(new FileWriter(dirPath + "/Documents/number.txt"));
        String testStringNumber = "4" + System.getProperty("line.separator") + "1"
                + System.getProperty("line.separator") + "2" + System.getProperty("line.separator") + "70"
                + System.getProperty("line.separator");
        w.write(testStringNumber);
        w.close();
    }

    @Test
    public void testTooManyArgs() throws Exception {
        try {
            appArgs.add("one");
            appArgs.add("two");
            appArgs.add("three");
            SORT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("sort: too many arguments", e.getMessage());
        }
    }

    @Test
    public void testMissingInput() throws Exception {
        try {
            SORT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("sort: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testArgsNotDashR() throws Exception {
        try {
            appArgs.add("-d");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            SORT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("sort: wrong arguments", e.getMessage());
        }
    }

    @Test
    public void testInvalidFilePath() {
        try {
            appArgs.add("Random");
            SORT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("sort: " + "Random does not exist", e.getMessage());
        }
    }

    @Test
    public void testNotFilePath() {
        try {
            appArgs.add(dirPath + System.getProperty("file.separator") + "Other");
            SORT.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("sort: " + dirPath + System.getProperty("file.separator") + "Other is a directory",
                    e.getMessage());
        }
    }

    @Test
    public void testEmptyFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test8.txt");
        SORT.exec(appArgs, null, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testInputStreamWithDashR() throws Exception {
        ByteArrayOutputStream bwi = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bwi));
        String expectedInput = "abhishek" + System.getProperty("line.separator") + "chitransh"
                + System.getProperty("line.separator") + "satish" + System.getProperty("line.separator") + "rajan"
                + System.getProperty("line.separator") + "naveen" + System.getProperty("line.separator") + "divyam"
                + System.getProperty("line.separator") + "harsh" + System.getProperty("line.separator");                                                        
        writer.write(expectedInput);
        writer.flush();
        writer.close();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(bwi.toByteArray());
        appArgs.add("-r");
        SORT.exec(appArgs, testInputStream, out);
        String expectedOutput = "satish" + System.getProperty("line.separator") + "rajan"
                + System.getProperty("line.separator") + "naveen" + System.getProperty("line.separator") + "harsh"
                + System.getProperty("line.separator") + "divyam" + System.getProperty("line.separator") + "chitransh"
                + System.getProperty("line.separator") + "abhishek" + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testInputStream() throws Exception {
        ByteArrayOutputStream bwi = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(bwi));
        String expectedInput = "abhishek" + System.getProperty("line.separator") + "chitransh"
                + System.getProperty("line.separator") + "satish" + System.getProperty("line.separator") + "rajan"
                + System.getProperty("line.separator") + "naveen" + System.getProperty("line.separator") + "divyam"
                + System.getProperty("line.separator") + "harsh" + System.getProperty("line.separator");
        writer.write(expectedInput);
        writer.flush();
        writer.close();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(bwi.toByteArray());
        SORT.exec(appArgs, testInputStream, out);
        String expectedOutput = "abhishek" + System.getProperty("line.separator") + "chitransh"
                + System.getProperty("line.separator") + "divyam" + System.getProperty("line.separator") + "harsh"
                + System.getProperty("line.separator") + "naveen" + System.getProperty("line.separator") + "rajan"
                + System.getProperty("line.separator") + "satish" + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testSimpleFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "simple.txt");
        String expectedOutput = "abhishek" + System.getProperty("line.separator") + "chitransh"
                + System.getProperty("line.separator") + "divyam" + System.getProperty("line.separator") + "harsh"
                + System.getProperty("line.separator") + "naveen" + System.getProperty("line.separator") + "rajan"
                + System.getProperty("line.separator") + "satish" + System.getProperty("line.separator");
        SORT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testMixFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "mix.txt");
        String expectedOutput = "1" + System.getProperty("line.separator") + "7" + System.getProperty("line.separator") + "Abc" + System.getProperty("line.separator") + "BALL"
                + System.getProperty("line.separator") + "abc" + System.getProperty("line.separator") + "apple"
                + System.getProperty("line.separator") + "bat" + System.getProperty("line.separator");
        SORT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testMixFileWithDashR() throws Exception {
        appArgs.add("-r");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "mix.txt");
        String expectedOutput = "bat" + System.getProperty("line.separator") + "apple" + System.getProperty("line.separator")
                + "abc" + System.getProperty("line.separator") + "BALL" + System.getProperty("line.separator") + "Abc"
                + System.getProperty("line.separator") + "7" + System.getProperty("line.separator") + "1"
                + System.getProperty("line.separator");
        SORT.exec(appArgs, null, out);
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testNumberFile() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "number.txt");
        String expectedOutput = "1" + System.getProperty("line.separator") + "2"
                + System.getProperty("line.separator") + "4" + System.getProperty("line.separator") + "70" + System.getProperty("line.separator");
        SORT.exec(appArgs, null, out);
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