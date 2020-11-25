package uk.ac.ucl.jsh.toolkit;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Globbing {
    private static final String currentDirectory = WorkingDr.getInstance().getWD();
    private ArrayList<String> globbingResult;
    private final String globbing_pattern;


    public Globbing(String globbing_path){
        this.globbing_pattern = globbing_path;
        get_globbed();
    }

    private void get_globbed(){
        globbingResult = new ArrayList<>();
        Path dir = Paths.get(currentDirectory);
        DirectoryStream<Path> stream;
        try {
            stream = Files.newDirectoryStream(dir, globbing_pattern);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
        for (Path entry : stream) {
            globbingResult.add(entry.getFileName().toString());
        }
        if (globbingResult.isEmpty()) {
            globbingResult.add(globbing_pattern);
        }
    }

    public ArrayList<String> getGlobbed_results() {
        return globbingResult;
    }

}
