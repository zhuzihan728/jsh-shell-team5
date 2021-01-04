package uk.ac.ucl.jsh.toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * TestFileHandle class helps to create the file and delete file in the working
 * directory of Jsh shell, it follows the singleton pattern
 */
public class TestFileHandle {
    /**
     * Utility function used to create a File Hierarchy that is used during the Test
     * 
     * @throws IOException Exception being thrown if the File hierarchy creation
     *                     fail
     */

    public void createTestFileHierarchy(String path, String fileName) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        // Check whether directory exists
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        Files.createFile(Paths.get(path + fileSeparator + fileName));
    }

    /**
     * Utility function used to delete a directory and all the files it contains
     * 
     * @param crtDirectory The directory that needs to be deleted
     */

    public void deleteFileHierarchy(File crtDirectory) {
        File[] files = crtDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteFileHierarchy(file);
            }
        }
        crtDirectory.delete();
    }

    /**
     * Utility function used to generate the files with multiple lines that are used
     * for testing the applications
     * 
     * @param lines Number of lines of text to generate
     * @return A string representing the whole content in the written file
     */
    public String generateLongFileText(int lines) {
        String resultString = new String();
        for (int i = 0; i < lines; ++i) {
            resultString += "Line" + Integer.toString(i) + System.getProperty("line.separator");
        }
        return resultString;
    }

}