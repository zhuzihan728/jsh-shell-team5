package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;

import org.junit.Test;
import uk.ac.ucl.jsh.call_parts.Substitution;

public class SubstituteTest {
    private static Substitution sub;

    @Before
    public void SetClass() throws Exception {
        sub = new Substitution("`echo foo`");
    }

    @Test
    public void testGetString() throws Exception {
        assertEquals("foo", sub.getString());
    }

    @Test
    public void testGetType() throws Exception {
        String a = "Substitution";
        assertEquals(a, sub.getType());
    }

    @Test
    public void testGetIn() throws Exception {
        assertEquals(null, sub.getInput());
    }

    @Test
    public void testGetOut() throws Exception {
        assertEquals(sub.compute(), sub.getOutput());
    }

    @Test
    public void testGetArray() throws Exception {
        ArrayList<String> a = new ArrayList<>();
        a.add("foo");
        assertEquals(a, sub.get_OutputArray());
    }

}