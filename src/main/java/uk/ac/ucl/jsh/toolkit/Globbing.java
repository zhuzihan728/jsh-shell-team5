package uk.ac.ucl.jsh.toolkit;

import uk.ac.ucl.jsh.toolkit.JshException;
import uk.ac.ucl.jsh.toolkit.WorkingDr;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Globbing {
    private String file_separator = System.getProperty("file.separator");
    private static final String currentDirectory = WorkingDr.getInstance().getWD();
    private ArrayList<String> globbingResult;
    private final String globbing_pattern;
    private boolean exist_globbing;


    public Globbing(String globbing_path) throws JshException {
        this.globbing_pattern = globbing_path;
        get_globbed();
    }

    private void get_globbed() throws JshException {
        exist_globbing = true;
        globbingResult = new ArrayList<>();
        Path dir = Paths.get(currentDirectory);
        DirectoryStream<Path> stream;
        try {
            stream = Files.newDirectoryStream(dir, globbing_pattern);
        }catch (IOException e){
            throw new JshException(e.getMessage());
        }
        for (Path entry : stream) {
            globbingResult.add(entry.getFileName().toString());
        }
        if (globbingResult.isEmpty()) {
            exist_globbing = false;
        }
    }

    public ArrayList<String> getGlobbed_results() {
        return globbingResult;
    }

    public boolean exist_globbing(){
        return exist_globbing;
    }

}
