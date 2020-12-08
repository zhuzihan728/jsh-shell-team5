package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ucl.jsh.applications.Pwd;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

public class PwdTest {
    private static final Pwd PWD = new Pwd();
    private static ArrayList<String> appArgs = new ArrayList<>();
    private static WorkingDr workingDir;
    private static String initWorkingDir;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void SetTest() {
        out = new ByteArrayOutputStream();
        workingDir = WorkingDr.getInstance();
        initWorkingDir = workingDir.getWD();
    }

    @Test
    public  void InvaildArgs(){
        appArgs.add("random");
        try {
            PWD.exec(appArgs, System.in, out);
        } catch (Exception e) {
            assertEquals("pwd: too many arguements", e.getMessage());
        }
    }

    @Test
    public void CurrentDir() throws Exception{
        appArgs.clear();
        out.reset();
        workingDir.setWD(System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator") + "test");
        PWD.exec(appArgs, System.in, out);
        assertEquals(System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator") + "test" + System.getProperty("line.separator"), out.toString()); 
    }

    @Test
    public  void RootDir() throws Exception{
        appArgs.clear();
        out.reset();
        workingDir.setWD(System.getProperty("file.separator"));
        PWD.exec(appArgs, System.in, out);
        assertEquals(System.getProperty("file.separator") + System.getProperty("line.separator"), out.toString()); 
    }

    //clear buffer, clean the app argument and reset the outputstream
    @AfterClass
    public static void EndTest() throws IOException {
        out.close();
        out.reset();
        appArgs.clear();
        out.reset();
        workingDir.setWD(initWorkingDir);
    }
}
