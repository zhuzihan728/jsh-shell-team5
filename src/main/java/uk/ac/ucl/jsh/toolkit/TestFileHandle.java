package uk.ac.ucl.jsh.toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TestFileHandle {
    

    /**
     * Utility function used to delete a directory and all the files it contains
     * 
     * @param crtDirectory The directory that needs to be deleted
     */
    public void deleteDirectory(File crtDirectory) {
        File[] files = crtDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteDirectory(file);
            }
         }
        crtDirectory.delete();
    }

        /**
     * Utility function used to create a File Hierarchy that is used by the Test classes
     * 
     * @throws IOException Exception being thrown if the File hierearchy fails to create
     */
    public void createTestFileHierarchy(String path, String fileName) throws IOException {
        // Create two utilities Strings
        String fileSeparator = System.getProperty("file.separator");
        // Check whether directory exists
        File file=new File(path);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        } 
        
        Files.createFile(Paths.get(path + fileSeparator + fileName));
       
   }
}
