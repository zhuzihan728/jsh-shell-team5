package uk.ac.ucl.jsh.toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Globbing {
    private final String file_separator = System.getProperty("file.separator");
    private static final String currentDirectory = WorkingDr.getInstance().getWD();
    private ArrayList<String> globbingResult;
    private final String globbing_pattern;


    public Globbing(String globbing_path){
        this.globbing_pattern = globbing_path;
        get_globbed();
    }

    private void get_globbed(){
        globbingResult = new ArrayList<>();
        DirectoryStream<Path> stream;
        File file = new File(currentDirectory + file_separator + globbing_pattern);
        Path dir = Paths.get(file.getParent());
        try {
            stream = Files.newDirectoryStream(dir, file.getName());
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }

        for (Path entry : stream) {
            String relative_path = entry.toAbsolutePath().toString().replace(currentDirectory,"");
            globbingResult.add(relative_path.substring(1));
        }
        if (globbingResult.isEmpty()) {
            globbingResult.add(globbing_pattern);
        }
    }

    public ArrayList<String> getGlobbed_results() {
        return globbingResult;
    }

}
