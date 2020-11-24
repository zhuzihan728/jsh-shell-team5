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

public class InputReader {
    public static ArrayList<String> input_List(Scanner in) {
        ArrayList<String> listoflines = new ArrayList<>();
        while(in.hasNextLine()) {
            listoflines.add(in.nextLine());
        }
        return listoflines;
    }


    public static Scanner file_reader(String file) throws IOException {
        Path filePath = Paths.get(WorkingDr.getInstance().getWD()).resolve(file);
        if (Files.exists(filePath)){
            Charset encoding = StandardCharsets.UTF_8;
            Scanner reader = new Scanner(filePath, encoding);
            return reader;
        }
        else{
            throw new NoSuchFileException(file+ " does not exist");
        }
    }

    public static ArrayList<String> fileContent_List(String file) throws IOException {
        return input_List(file_reader(file));
    }

    public static File getFile(String filename){
        return new File(getFilepath(filename));
    }

    private static String getFilepath(String fileName){
        return WorkingDr.getInstance().getWD() + System.getProperty("file.separator")
                + fileName;
    }




}