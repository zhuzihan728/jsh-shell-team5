package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

public class JshTest {
    public JshTest() {
    }

    @Test
    public void testJsh() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        JshMain.runJsh("echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"foo");
    }

    @Test
    public void testEcho() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        //freestyle Echo
        JshMain.runJsh("echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"foo");
        //with double quotes 
        JshMain.runJsh("echo \"foo\"", out);
        assertEquals(scn.next(),"foo");
        //null
        JshMain.runJsh("echo ", out);
        assertEquals(scn.next(),null);
        //emty string
        JshMain.runJsh("echo \"\"", out);
        assertEquals(scn.next(),"");
        //
    } 

}
