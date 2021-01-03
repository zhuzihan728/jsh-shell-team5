package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import uk.ac.ucl.jsh.applications.Grep;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GrepTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Grep GREP = new Grep();
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
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test1.txt");
            testFileHandle.createTestFileHierarchy(dirPath + "/Other", "test3.txt");
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
        
        bw = new BufferedWriter(new FileWriter(dirPath + "/Documents/test1.txt"));
        bw.write("Hello World");
        bw.close();

        bw = new BufferedWriter(new FileWriter(dirPath + "/Documents/test2.txt"));
        bw.write("This is a test");
        bw.close();

        bw = new BufferedWriter(new FileWriter(dirPath + "/Other/test3.txt"));
        bw.write("This is an other test");
        bw.close();
    
    }

    @Test
    public void testMissingArgu() throws Exception {
        try {
            GREP.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("grep: missing argument", e.getMessage());
        }
    }

    @Test
    public void testMissingInput() throws Exception {
        try {
            appArgs.add("pattern");
            GREP.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("grep: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testInvailPath() throws Exception {
        try {
            appArgs.add("pattern");
            appArgs.add("Random");
            GREP.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("grep: " + "Random does not exist", e.getMessage());
        }
    }

    @Test
    public void testNotFilePath() {
        try {
            appArgs.add("pattern");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
            GREP.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("grep: " + dirPath + System.getProperty("file.separator") + "Documents is a directory",
                    e.getMessage());
        }
    }

    @Test
    public void testNotExistFile() {
        try {
            appArgs.add("pattern");
            appArgs.add(dirPath + System.getProperty("file.separator") + "Random");
            GREP.exec(appArgs, null, out);
        } catch (Exception e) {
            assertEquals("grep: " + dirPath + System.getProperty("file.separator") + "Random does not exist",
                    e.getMessage());
        }
    }

    @Test
    public void testEmptyFile() throws Exception {
        appArgs.add(".*");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test8.txt");
        GREP.exec(appArgs, null, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testReadingFromInputStream() throws Exception {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outstream));
        String inputStream = "Test one" + System.getProperty("line.separator") + "Hello Test"
                + System.getProperty("line.separator") + "Not Me" + System.getProperty("line.separator")
                + System.getProperty("line.separator");
        bw.write(inputStream);
        bw.flush();
        bw.close();
        ByteArrayInputStream testInput = new ByteArrayInputStream(outstream.toByteArray());
        appArgs.add("Test");
        GREP.exec(appArgs, testInput, out);
        String expectedOutput = "Test one" + System.getProperty("line.separator") + "Hello Test"
                + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testReadingFromInputStreamWithColon() throws Exception {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outstream));
        String inputStream = "Test one" + System.getProperty("line.separator") + "Hello Test"
                + System.getProperty("line.separator") + "Not Me" + System.getProperty("line.separator") + "Again, Test"
                + System.getProperty("line.separator") + System.getProperty("line.separator");
        bw.write(inputStream);
        bw.flush();
        bw.close();
        ByteArrayInputStream testInput = new ByteArrayInputStream(outstream.toByteArray());
        appArgs.add("Test");
        GREP.exec(appArgs, testInput, out);
        String expectedOutput = "Test one" + System.getProperty("line.separator") + "Hello Test"
                + System.getProperty("line.separator") + "Again, Test" + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testDirectoryPath() throws Exception {
        appArgs.add("Lin.*");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test4.txt");
        GREP.exec(appArgs, null, out);
        String expectedOutput = new String();
        for (int i = 0; i < 30; ++i) {
            expectedOutput += "Line" + Integer.toString(i) + System.getProperty("line.separator");
        }
        assertEquals(expectedOutput, out.toString());
    } 

    @Test
    public void testMultipleFiles() throws Exception {
        appArgs.add("test");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Other" + System.getProperty("file.separator")
                + "test3.txt");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test1.txt");
        GREP.exec(appArgs, System.in, out);
        String expectedOutput = "This is an other test";
        assertTrue(out.toString().contains(expectedOutput));
    }

    // @Test
    // public void testFileWithInputStream() throws Exception {
    //     appArgs.add("Test");
    //     appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "test4.txt"); 
    //     ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    //     BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outstream));
    //     String inputStream = "Test one" + System.getProperty("line.separator") + "Hello Test"
    //             + System.getProperty("line.separator") + "Not Me" + System.getProperty("line.separator")
    //             + System.getProperty("line.separator");
    //     bw.write(inputStream);
    //     bw.flush();
    //     bw.close();
    //     ByteArrayInputStream testInput = new ByteArrayInputStream(outstream.toByteArray());
    //     GREP.exec(appArgs, testInput, out);
    //     String expectedOutput = "Test one" + System.getProperty("line.separator") + "Hello Test" + System.getProperty("line.separator");
    //     assertEquals(expectedOutput, out.toString());
    // }







    // testReadFromGlobbedPath testWithGlobbedArguments testMultipleFIlesFromGlobbedArgument





    @After
    // Delete the test hierarchy, reset the command arguments and reset the
    // outputstream
    public void afterTest() throws IOException {
        out.reset();
        appArgs.clear();
        File path = new File(dirPath);
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
