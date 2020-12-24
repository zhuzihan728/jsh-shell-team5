package uk.ac.ucl.jsh.toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TestFileHandle {
    public void createTestFileHierarchy(String path, String fileName) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        // Check whether directory exists
        File file=new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        } 
        Files.createFile(Paths.get(path + fileSeparator + fileName));
   }

   public void deleteFileHierarchy(File crtDirectory) {
    File[] files = crtDirectory.listFiles();
    if (files != null) {
        for (File file : files) {
            deleteFileHierarchy(file);
        }
     }
    crtDirectory.delete();   
    }
}