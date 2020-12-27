package uk.ac.ucl.jsh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    
    @BeforeClass
    public static void SetTest() {
        appArgs = new ArrayList<>();
        out = new ByteArrayOutputStream();
        workingDir = WorkingDr.getInstance();
        dirPath = workingDir.getWD() + "/tmp/Test";
    }

    // @Before
    // public void createFile() throws Exception {
    //     TestFileHandle testFileHandle = new TestFileHandle();
    //     try {
    //         testFileHandle.createTestFileHierarchy(dirPath + "/Documents", "test4.txt");
    //     } catch (Exception e) {
    //     }
    //     Files.createFile(Paths.get(dirPath + System.getProperty("file.separator") + "/Documents", "test8.txt"));
    //     testFileHandle.createTestFileHierarchy(dirPath + "/Other", "test.out");

    //     new File(dirPath + "/Documents/test4.txt").createNewFile();
    //     BufferedWriter bw = new BufferedWriter(new FileWriter(dirPath + "/Documents/test4.txt"));
    //     bw.write(testFileHandle.generateLongFileText(30));
    //     bw.close();
    // }




// testInvalidNumberOfArgumentsMissingArguments testMissingInput
// testInvalidArgumentInvalidPath testReadingFromDirectoryPath
// testReadFromEmptyFile testReadFromInputStream
// testReadFromInputStreamWihComplexRegex testReadFromFileAbsolutePath
// testReadFromMultipleFiles readFromFileWithInputStreamNotNull
// testReadFromGlobbedPath testWithGlobbedArguments
// testMultipleFIlesFromGlobbedArgument


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
        workingDir.setWD(dirPath);
    }
}
