package uk.ac.ucl.jsh.toolkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class InputReader {
    public static ArrayList<String> read_stdin() {
        Scanner in = new Scanner(System.in);//do not close
        ArrayList<String> listoflines = new ArrayList<>();
        while(in.hasNext()) {
            listoflines.add(in.nextLine());
        }
        return listoflines;
    }


    public static BufferedReader file_reader(String file) throws IOException {
        Path filePath = Paths.get(WorkingDr.getInstance().getWD()).resolve(file);
        if (Files.exists(filePath)){
            Charset encoding = StandardCharsets.UTF_8;
            BufferedReader reader = Files.newBufferedReader(filePath, encoding);
            return reader;
        }
        else{
            throw new RuntimeException();
        }

    }



    public static ArrayList<String> read_file(String file) throws IOException {
        BufferedReader reader = file_reader(file);
        ArrayList<String> listoflines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            listoflines.add(line);
        }
        reader.close();
        return listoflines;
    }

    
}