package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.ucl.jsh.applications.AppFactory;
import uk.ac.ucl.jsh.applications.Application;

public class AppFactoryTest {
    
    @Test
    public void testCd() throws Exception{
        AppFactory app = new AppFactory();
        Application cd = app.getApplication("cd");
        assertEquals(uk.ac.ucl.jsh.applications.Cd.class, cd.getClass());
    }

    @Test
    public void testCat() throws Exception {
        AppFactory app = new AppFactory();
        Application cat = app.getApplication("cat");
        assertEquals(uk.ac.ucl.jsh.applications.Cat.class, cat.getClass());
    }
}
