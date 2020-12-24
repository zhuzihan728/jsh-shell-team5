package uk.ac.ucl.jsh.toolkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * If an argument that is not single quoted or double quoted and contains *, that argument
 * needs to be globbed into filenames that follows the pattern.
 */
public class Globbing {
    /**
     * Provides the system-dependent file seperator that is used later in getting the 
     * file path.
     */
    private final String file_separator = System.getProperty("file.separator");

    /**
     * Address of the current working directory.
     */
    private static final String currentDirectory = WorkingDr.getInstance().getWD();

    /**
     * List of strings that follows the globbing pattern provided.
     */
    private ArrayList<String> globbingResult;

    /**
     * Globbing pattern received during the construction of the class.
     */
    private final String globbing_pattern;


    /**
     * Initialise the Globbing class using the globbing pattern provided.
     * @param globbing_path Filename pattern provided in the raw command.
     */
    public Globbing(String globbing_path){
        this.globbing_pattern = globbing_path;
        get_globbed();
    }

    /**
     * Get the globbed filename arguments from the file system using the patterns.
     */
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

    /**
     * Public function that allow other part of the code to get the result.
     * @return ArrayList of globbed filenames.
     */
    public ArrayList<String> getGlobbed_results() {
        return globbingResult;
    }

}
