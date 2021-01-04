package uk.ac.ucl.jsh;

import org.junit.*;
import uk.ac.ucl.jsh.applications.Cat;
import uk.ac.ucl.jsh.toolkit.TestFileHandle;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CatTest {
    private static ArrayList<String> appArgs;
    private static WorkingDr workingDir;
    private static String dirPath;
    private static final Cat CAT = new Cat();
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

    public static String cre(String path) {
        int i = path.lastIndexOf("/");
        File file = new File(i == -1 ? path : path.substring(0, i));
        if (!file.exists()) {
            file.mkdirs();
        }
        if (path.contains(".")) {
            file = new File(path);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return path;
    }

    @Before
    public void createFile() throws Exception {
        TestFileHandle testFileHandle = new TestFileHandle();
        try {
            testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test1.txt");
            testFileHandle.createTestFileHierarchy(dirPath + "/Other", "test3.txt");
        } catch (Exception e) {
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(cre(dirPath + "/Documents/test1.txt")));
        bw.write("Hello World");
        bw.close();

        bw = new BufferedWriter(new FileWriter(cre(dirPath + "/Documents/test2.txt")));
        bw.write("This is a test");
        bw.close();

        bw = new BufferedWriter(new FileWriter(cre(dirPath + "/Other/test3.txt")));
        bw.write("This is an other test");
        bw.close();

    }

    @Test
    public void testMissingInput() throws Exception {
        try {
            CAT.exec(appArgs, null, out);
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
            String ExpectMessage = "cat: " + dirPath + System.getProperty("file.separator") + "RandomPath"
                    + " does not exist";
            assertEquals(ExpectMessage, e.getMessage());
        }
    }

    @Test
    public void testDirectoryPath() throws Exception {
        try {
            appArgs.add(cre(dirPath + System.getProperty("file.separator") + "Documents"));
            CAT.exec(appArgs, System.in, out);
        } catch (Exception e) {
            String dirExpectMessage = "cat: " + dirPath + System.getProperty("file.separator") + "Documents"
                    + " is a directory";
            assertEquals(dirExpectMessage, e.getMessage());
        }
    }

    @Test
    public void testReadingFromInputStream() throws Exception {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outstream));
        String expectedOutput = "New" + System.getProperty("line.separator") + " Input"
                + System.getProperty("line.separator");
        bw.write(expectedOutput);
        bw.flush();
        bw.close();
        ByteArrayInputStream testInput = new ByteArrayInputStream(outstream.toByteArray());
        CAT.exec(appArgs, testInput, out);
        assertEquals(expectedOutput, outstream.toString());
    }

    @Test
    public void testAbsoluteFilePath() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test1.txt");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test2.txt");
        CAT.exec(appArgs, System.in, out);
        String expectedOutput = "Hello World" + System.getProperty("line.separator") + "This is a test"
                + System.getProperty("line.separator");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testMultipleFiles() throws Exception {
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test1.txt");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
                + "test2.txt");
        appArgs.add(dirPath + System.getProperty("file.separator") + "Other" + System.getProperty("file.separator")
                + "test3.txt");
        CAT.exec(appArgs, System.in, out);
        String expectedOutput = "Hello World" + System.getProperty("line.separator") + "This is a test"
                + System.getProperty("line.separator") + System.getProperty("line.separator") + "This is an other test"
                + System.getProperty("line.separator") + System.getProperty("line.separator");
        assertEquals(expectedOutput, expectedOutput.toString());
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
