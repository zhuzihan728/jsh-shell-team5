package uk.ac.ucl.jsh.toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * InputReader deals with file and input stream, it helps to create scanners and
 * read input
 */
public class InputReader {

    /**
     * a method to read input from a scanner and returns an arrayList storing all
     * lines read
     * 
     * @param in The scanner of file/input stream
     * @return ArrayList<String> The arrayList storing all lines read from the
     *         scanner
     */
    public static ArrayList<String> input_List(Scanner in) {
        ArrayList<String> listOfLines = new ArrayList<>();
        while (in.hasNextLine()) {
            listOfLines.add(in.nextLine());
        }
        return listOfLines;
    }

    /**
     * a method that takes a file name ane returns a scanner of that file
     * 
     * @param file The name of the file as a string
     * @return Scanner The scanner of the file
     * @throws IOException Exception thrown when file of that name does not exist
     */
    public static Scanner file_reader(String file) throws IOException {
        Path filePath = Paths.get(WorkingDr.getInstance().getWD()).resolve(file);
        if(Files.isDirectory(filePath)){
            throw new NoSuchFileException(file + " is a directory");
        }
        if (Files.exists(filePath)) {
            Charset encoding = StandardCharsets.UTF_8;
            Scanner reader = new Scanner(filePath, encoding);
            return reader;
        } else {
            throw new NoSuchFileException(file + " does not exist");
        }
    }

    /**
     * a method that takes a file name and returns an arrayList containing all lines
     * of the file
     * 
     * @param file The name of the file as a string
     * @return ArrayList<String> The arrayList containing the file content
     * @throws IOException Exception thrown when file of that name does not exist
     */
    public static ArrayList<String> fileContent_List(String file) throws IOException {
        return input_List(file_reader(file));
    }

    /**
     * a method that takes a file name and return the File object
     * 
     * @param filename The name of the file as a string
     * @return File The File object
     */
    public static File getFile(String filename) {
        return new File(getFilepath(filename));
    }

    /**
     * a methods that takes a file name and returns the path of the file relative to
     * current working directory
     * 
     * @param fileName The name of the file as a string
     * @return String The path of the file relative to current working directory as
     *         a string
     */
    private static String getFilepath(String fileName) {
        return WorkingDr.getInstance().getWD() + System.getProperty("file.separator") + fileName;
    }

}