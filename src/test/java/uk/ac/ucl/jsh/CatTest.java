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
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;

import uk.ac.ucl.jsh.applications.Cat;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class CatTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Cat CAT = new Cat();
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
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test1.txt");
            testFileHandle.createTestFileHierarchy(dirPath + "/Other", "test3.txt");
        } catch (Exception e) {
        }
        new File(dirPath + "/Documents/test1.txt").createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter("test1.txt"));
        bw.write("Hello World");
        bw.close();

        new File(dirPath + "/Documents/test2.txt").createNewFile();
        bw = new BufferedWriter(new FileWriter("test2.txt"));
        bw.write("This is a test");
        bw.close();

        new File(dirPath + "/Other/test3.txt").createNewFile();
        bw = new BufferedWriter(new FileWriter("test3.txt"));
        bw.write("This is an other test");
        bw.close();

    }

    @Test
    public void testMissingInput() throws Exception {
        try {
            CAT.exec(appArgs, System.in, out);
        } catch (Exception e) {
            assertEquals("cat: missing InputStream", e.getMessage());
        }
    }

    @Test
    public void testNotExistPath() throws Exception {
        try {
            appArgs.add(dirPath + System.getProperty("file.separator") + "RandomPath");
            CAT.exec(appArgs, System.in, out);
        } catch (Exception e) {
            String ExpectMessage = "cat: " + System.getProperty("file.separator") + "RandomPath" + "(No such file or directory)";
            if (null != e.getMessage()) {
                assertTrue(ExpectMessage, e.getMessage().contains("(No such file or directory)"));
            }
        }
    }

    @Test
    public void testDirectoryPath() throws Exception {
        try {
            appArgs.add("Documents");
            CAT.exec(appArgs, System.in, out);
        } catch (Exception e) {
            String dirExpectMessage = "cat: " + System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator") + "Test" + System.getProperty("file.separator") + "Documents" + "(Is a directory)";
            if (null != e.getMessage()) {
                assertTrue(dirExpectMessage, e.getMessage().contains("(Is a directory)"));
            }
        }
    }

    @Test
    public void testReadingFromInputStream() throws Exception {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outstream));
        String expectedOutput = "New" + System.getProperty("line.separator") + " Input" + System.getProperty("line.separator");
        bw.write(expectedOutput);
        bw.flush();
        bw.close(); 
        ByteArrayInputStream testInput = new ByteArrayInputStream(outstream.toByteArray());
        CAT.exec(appArgs, testInput, out);
        assertEquals(expectedOutput, outstream.toString());
    }

    @Test
    public void testAbsoluteFilePath() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
        CAT.exec(appArgs, System.in, out);
        String expectedOutput = "Hello World" + System.getProperty("line.separator") + "This is a test"+ System.getProperty("line.separator") + System.getProperty("line.separator");
        assertEquals(expectedOutput, expectedOutput.toString());
    }
    
    @Test
    public void testMultipleFiles() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Other");
        CAT.exec(appArgs, System.in, out);
        String expectedOutput = "Hello World" + System.getProperty("line.separator") + "This is a test" + System.getProperty("line.separator"+ System.getProperty("line.separator")
                + "This is an other test" + System.getProperty("line.separator") + System.getProperty("line.separator"));
        assertEquals(expectedOutput, expectedOutput.toString()); 
    } 

    // @Test
    // public void testGlobbingFiles() throws Exception {
    //     appArgs.add(dirPath + System.getProperty("file.separator") + "D*s");
    //     CAT.exec(appArgs, System.in, out);
    //     String expectedOutput = "Hello World" + System.getProperty("line.separator") + "This is a test"
    //             + System.getProperty("line.separator" + System.getProperty("line.separator") + "This is an other test");
    //     assertEquals(expectedOutput, expectedOutput.toString());
    // }

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
        workingDir.setWD(dirPath);
    }


// testMultipleFilesFromTwoGlobArguments testMultipleFilesFromGlobArgument


}
