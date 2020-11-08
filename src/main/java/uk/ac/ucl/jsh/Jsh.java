package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Jsh {

    private static String currentDirectory = System.getProperty("user.dir");

    public static void eval(String cmdline, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);

        CharStream parserInput = CharStreams.fromString(cmdline); 
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);        
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        ParseTree tree = parser.command();
        ArrayList<String> rawCommands = new ArrayList<String>();
        String lastSubcommand = "";
        for (int i=0; i<tree.getChildCount(); i++) {
            if (!tree.getChild(i).getText().equals(";")) {
                lastSubcommand += tree.getChild(i).getText();
            } else {
                rawCommands.add(lastSubcommand);
                lastSubcommand = "";
            }
        }
        rawCommands.add(lastSubcommand);

        for (String rawCommand : rawCommands) {
            String spaceRegex = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
            ArrayList<String> tokens = new ArrayList<String>();
            Pattern regex = Pattern.compile(spaceRegex);
            Matcher regexMatcher = regex.matcher(rawCommand);
            String nonQuote;
            while (regexMatcher.find()) {
                if (regexMatcher.group(1) != null || regexMatcher.group(2) != null) {
                    String quoted = regexMatcher.group(0).trim();
                    tokens.add(quoted.substring(1,quoted.length()-1));
                } else {
                    nonQuote = regexMatcher.group().trim();
                    ArrayList<String> globbingResult = new ArrayList<String>();
                    Path dir = Paths.get(currentDirectory);
                    DirectoryStream<Path> stream = Files.newDirectoryStream(dir, nonQuote);
                    for (Path entry : stream) {
                        globbingResult.add(entry.getFileName().toString());
                    }
                    if (globbingResult.isEmpty()) {
                        globbingResult.add(nonQuote);
                    }
                    tokens.addAll(globbingResult);
                }
            }
            String appName = tokens.get(0);
            ArrayList<String> appArgs = new ArrayList<String>(tokens.subList(1, tokens.size()));
            switch (appName) {
            case "cd":
                if (appArgs.isEmpty()) {
                    throw new RuntimeException("cd: missing argument");
                } else if (appArgs.size() > 1) {
                    throw new RuntimeException("cd: too many arguments");
                }
                String dirString = appArgs.get(0);
                File dir = new File(currentDirectory, dirString);
                if (!dir.exists() || !dir.isDirectory()) {
                    throw new RuntimeException("cd: " + dirString + " is not an existing directory");
                }
                currentDirectory = dir.getCanonicalPath();
                break;
            case "find":
                File path;
                String pattern;
                if (appArgs.isEmpty() || appArgs.size() == 1) {
                    throw new RuntimeException("find: missing argument");}              
                else if (appArgs.size() == 2){
                    if(appArgs.get(0).equals("-name")){
                        path = new File(currentDirectory);
                        pattern = appArgs.get(1).replaceAll("\\*", ".*");
                    }
                    else{
                        throw new RuntimeException("find: wrong arguments");}                
                }
                else if (appArgs.size() == 3 ){
                    if(appArgs.get(1).equals("-name")){
                        path = new File(appArgs.get(0));
                        pattern = appArgs.get(2).replaceAll("\\*", ".*");
                    }
                    else{
                        throw new RuntimeException("find: wrong arguments");} 
                }
                else {
                    throw new RuntimeException("find: too many arguments");
                }
        /*                      ↑上面确定path和pattern                     */               
                try {
                    boolean atLeastOnePrinted = getFiles(path, pattern, path, writer);
                                   
                    if (atLeastOnePrinted) {
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                    else{
                        throw new RuntimeException("find: no such file or directory");
                    }
                } catch (NullPointerException e) {
                    throw new RuntimeException("find: no such directory");
                }
                break;
                

            case "pwd":
                writer.write(currentDirectory);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                break;
            case "ls":
                File currDir;
                if (appArgs.isEmpty()) {
                    currDir = new File(currentDirectory);
                } else if (appArgs.size() == 1) {
                    currDir = new File(appArgs.get(0));
                } else {
                    throw new RuntimeException("ls: too many arguments");
                }
                try {
                    File[] listOfFiles = currDir.listFiles();
                    boolean atLeastOnePrinted = false;
                    for (File file : listOfFiles) {
                        if (!file.getName().startsWith(".")) {
                            writer.write(file.getName());
                            writer.write("\t");
                            writer.flush();
                            atLeastOnePrinted = true;
                        }
                    }
                    if (atLeastOnePrinted) {
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                } catch (NullPointerException e) {
                    throw new RuntimeException("ls: no such directory");
                }
                break;
            case "cat":
                if (appArgs.isEmpty()) {
                    throw new RuntimeException("cat: missing arguments");
                } else {
                    for (String arg : appArgs) {
                        Charset encoding = StandardCharsets.UTF_8;
                        File currFile = new File(currentDirectory + File.separator + arg);
                        if (currFile.exists()) {
                            Path filePath = Paths.get(currentDirectory + File.separator + arg);
                            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                    writer.write(String.valueOf(line));
                                    writer.write(System.getProperty("line.separator"));
                                    writer.flush();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException("cat: cannot open " + arg);
                            }
                        } else {
                            throw new RuntimeException("cat: file does not exist");
                        }
                    }
                }
                break;
            case "echo":
                boolean atLeastOnePrinted = false;
                for (String arg : appArgs) {
                    writer.write(arg);
                    writer.write(" ");
                    writer.flush();
                    atLeastOnePrinted = true;
                }
                if (atLeastOnePrinted) {
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                }
                break;
            case "head":
                if (appArgs.isEmpty()) {
                    throw new RuntimeException("head: missing arguments");
                }
                if (appArgs.size() != 1 && appArgs.size() != 3) {
                    throw new RuntimeException("head: wrong arguments");
                }
                if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
                    throw new RuntimeException("head: wrong argument " + appArgs.get(0));
                }
                int headLines = 10;
                String headArg;
                if (appArgs.size() == 3) {
                    try {
                        headLines = Integer.parseInt(appArgs.get(1));
                    } catch (Exception e) {
                        throw new RuntimeException("head: wrong argument " + appArgs.get(1));
                    }
                    headArg = appArgs.get(2);
                } else {
                    headArg = appArgs.get(0);
                }
                File headFile = new File(currentDirectory + File.separator + headArg);
                if (headFile.exists()) {
                    Charset encoding = StandardCharsets.UTF_8;
                    Path filePath = Paths.get((String) currentDirectory + File.separator + headArg);
                    try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                        for (int i = 0; i < headLines; i++) {
                            String line = null;
                            if ((line = reader.readLine()) != null) {
                                writer.write(line);
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("head: cannot open " + headArg);
                    }
                } else {
                    throw new RuntimeException("head: " + headArg + " does not exist");
                }
                break;
            case "tail":
                if (appArgs.isEmpty()) {
                    throw new RuntimeException("tail: missing arguments");
                }
                if (appArgs.size() != 1 && appArgs.size() != 3) {
                    throw new RuntimeException("tail: wrong arguments");
                }
                if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
                    throw new RuntimeException("tail: wrong argument " + appArgs.get(0));
                }
                int tailLines = 10;
                String tailArg;
                if (appArgs.size() == 3) {
                    try {
                        tailLines = Integer.parseInt(appArgs.get(1));
                    } catch (Exception e) {
                        throw new RuntimeException("tail: wrong argument " + appArgs.get(1));
                    }
                    tailArg = appArgs.get(2);
                } else {
                    tailArg = appArgs.get(0);
                }
                File tailFile = new File(currentDirectory + File.separator + tailArg);
                if (tailFile.exists()) {
                    Charset encoding = StandardCharsets.UTF_8;
                    Path filePath = Paths.get((String) currentDirectory + File.separator + tailArg);
                    ArrayList<String> storage = new ArrayList<>();
                    try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            storage.add(line);
                        }
                        int index = 0;
                        if (tailLines > storage.size()) {
                            index = 0;
                        } else {
                            index = storage.size() - tailLines;
                        }
                        for (int i = index; i < storage.size(); i++) {
                            writer.write(storage.get(i) + System.getProperty("line.separator"));
                            writer.flush();
                        }            
                    } catch (IOException e) {
                        throw new RuntimeException("tail: cannot open " + tailArg);
                    }
                } else {
                    throw new RuntimeException("tail: " + tailArg + " does not exist");
                }
                break;
            case "grep":
                if (appArgs.size() < 2) {
                    throw new RuntimeException("grep: wrong number of arguments");
                }
                Pattern grepPattern = Pattern.compile(appArgs.get(0));
                int numOfFiles = appArgs.size() - 1;
                Path filePath;
                Path[] filePathArray = new Path[numOfFiles];
                Path currentDir = Paths.get(currentDirectory);
                for (int i = 0; i < numOfFiles; i++) {
                    filePath = currentDir.resolve(appArgs.get(i + 1));
                    if (Files.notExists(filePath) || Files.isDirectory(filePath) || 
                        !Files.exists(filePath) || !Files.isReadable(filePath)) {
                        throw new RuntimeException("grep: wrong file argument");
                    }
                    filePathArray[i] = filePath;
                }
                for (int j = 0; j < filePathArray.length; j++) {
                    Charset encoding = StandardCharsets.UTF_8;
                    try (BufferedReader reader = Files.newBufferedReader(filePathArray[j], encoding)) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            Matcher matcher = grepPattern.matcher(line);
                            if (matcher.find()) {
                                if (numOfFiles > 1) {
                                    writer.write(appArgs.get(j+1));
                                    writer.write(":");
                                }
                                writer.write(line);
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("grep: cannot open " + appArgs.get(j + 1));
                    }
                }
                break;
            
            case "sort":
                if (appArgs.isEmpty()) {
                    writeToShell(read_stdin(), writer, false);
                }
                else if (appArgs.size() == 1) {
                    String sortArg = appArgs.get(0);
                    if(sortArg.equals("-r")){
                        writeToShell(read_stdin(), writer, true);
                    }
                    else {
                        Path curp = Paths.get(currentDirectory);
                        Path p = curp.resolve(sortArg);
                        if (Files.exists(p)){
                            try{writeToShell(read_file(p), writer, false);}
                            catch(IOException e){throw new RuntimeException("sort: cannot open " + sortArg);}
                        }
                        else{
                            throw new RuntimeException("sort: " + sortArg + " does not exist");
                        }
                    }      
                }
                else if (appArgs.size() == 2){
                    if(appArgs.get(0).equals("-r")){
                        String sortArg = appArgs.get(1);
                        Path curp = Paths.get(currentDirectory);
                        Path p = curp.resolve(sortArg);
                        if (Files.exists(p)){
                            try{writeToShell(read_file(p), writer, true);}
                            catch(IOException e){throw new RuntimeException("sort: cannot open " + sortArg);}
                        }
                        else{
                            throw new RuntimeException("sort: " + sortArg + " does not exist");
                        }
                    }
                    else{
                        throw new RuntimeException("sort: wrong arguments");
                    }

                }
                else {
                    throw new RuntimeException("sort: too many arguments");
                }                
                break;

            case "uniq":
                if (appArgs.isEmpty()) {
                    uniToShell(read_stdin(), writer, false);
                }
                else if (appArgs.size() == 1) {
                    String sortArg = appArgs.get(0);
                    if(sortArg.equals("-i")){
                        uniToShell(read_stdin(), writer, true);
                    }
                    else {
                        Path curp = Paths.get(currentDirectory);
                        Path p = curp.resolve(sortArg);
                        if (Files.exists(p)){
                            try{uniToShell(read_file(p), writer, false);}
                            catch(IOException e){throw new RuntimeException("uniq: cannot open " + sortArg);}
                        }
                        else{
                            throw new RuntimeException("uniq: " + sortArg + " does not exist");
                        }
                    }      
                }
                else if (appArgs.size() == 2){
                    if(appArgs.get(0).equals("-i")){
                        String sortArg = appArgs.get(1);
                        Path curp = Paths.get(currentDirectory);
                        Path p = curp.resolve(sortArg);
                        if (Files.exists(p)){
                            try{uniToShell(read_file(p), writer, true);}
                            catch(IOException e){throw new RuntimeException("uniq: cannot open " + sortArg);}
                        }
                        else{
                            throw new RuntimeException("uniq: " + sortArg + " does not exist");
                        }
                    }
                    else{
                        throw new RuntimeException("uniq: wrong arguments");
                    }

                }
                else {
                    throw new RuntimeException("uniq: too many arguments");
                }
                break;

            case "cut":
                String cutpat = "((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-))(,((\\d*[1-9]+\\d*)|(-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-\\d*[1-9]+\\d*)|(\\d*[1-9]+\\d*-)))*";
                if (appArgs.size() < 2) {
                    throw new RuntimeException("cut: missing arguments");
                }
                else if (appArgs.size() == 2){
                    if(appArgs.get(0).equals("-b")){
                        String option = appArgs.get(1);
                            if (matchPattern(option, cutpat)){
                                stdinToShell(writer, option);
                            }
                            else{
                                throw new RuntimeException("cut: wrong option argument");
                            }
                    
                    }
                    else{
                        throw new RuntimeException("cut: wrong argument");
                    }

                }
                else if (appArgs.size() == 3){
                    if(appArgs.get(0).equals("-b")){
                        String option = appArgs.get(1);
                        if (matchPattern(option, cutpat)){
                            String cutArg = appArgs.get(2);
                            Path curp = Paths.get(currentDirectory);
                            Path p = curp.resolve(cutArg);
                            if (Files.exists(p)){
                                try{cutfileToShell(read_file(p), writer, option);}
                                catch(IOException e){throw new RuntimeException("uniq: cannot open " + cutArg);}
                            }
                            else{
                                throw new RuntimeException("uniq: " + cutArg + " does not exist");
                            }
                        }
                        else{
                            throw new RuntimeException("cut: wrong option argument");
                        }

                    
                    }
                    else{
                        throw new RuntimeException("cut: wrong arguments");
                    }

                }
                else {
                    throw new RuntimeException("uniq: too many arguments");
                }
                break;
                
            

            default:
                throw new RuntimeException(appName + ": unknown application");
            }
        }
    }

// the methods used in cut

    static ArrayList<Integer> optionSplit(String option){
        String[] optionls = option.split(",");
        ArrayList<Integer> cutls = new ArrayList<Integer>();
        Integer toinf = 0;
        Integer from;
        Integer to; 
        ArrayList<Integer> out = new ArrayList<Integer>();
        for (String a : optionls){
            if(!a.contains("-")){
                from = Integer.parseInt(a);
                if(from>=toinf&&toinf!=0){
                    continue;
                }
                cutls.add(from);              
            }
            else{
                if(a.startsWith("-")){
                    to = Integer.parseInt(a.split("-")[1]);
                    if(to>=toinf&&toinf!=0){
                        return out = null;
                    }
                    for (int i=1; i<=to;i++){
                        cutls.add(i);

                    }                    
                }
                else if(a.endsWith("-")){
                    from = Integer.parseInt(a.split("-")[0]);
                    if(from>=toinf&&toinf!=0){
                        continue;
                    }
                    cutls.add(from);
                    if(!cutls.remove(toinf)){
                        cutls.add(Integer.MAX_VALUE);
                    }
                    toinf = from;                    
                }
                else{
                    String[] range = a.split("-");
                    from = Integer.parseInt(range[0]);
                    to = Integer.parseInt(range[1]);
                    for (int i=from; i<=to;i++){
                        if(i>=toinf&&toinf!=0){
                            break;
                        }
                        cutls.add(i);
                    }                    

                }


            }
        }
        cutls.sort(Integer::compareTo);
        for (Integer a : cutls){
            if(out.contains(a)){
                continue;
            }
            out.add(a);

        }
        return out;
    }

    static void stdinToShell(OutputStreamWriter writer, String option)  throws IOException{
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> cutnum = optionSplit(option);
        Integer toinf = 0;
        if(cutnum.contains(Integer.MAX_VALUE)){
            toinf = cutnum.get(cutnum.size()-2);
        }
        String input;
        String output;
        while(in.hasNext()) {
            output = "";
            input = in.nextLine();
            for (Integer i:cutnum){
                if(i > input.length()){
                    break;
                }
                if(i!=toinf){
                    output = output + String.valueOf(input.charAt(i-1));
                }
                else{
                    output = output + input.substring(i);
                    break;
                }

            }
            writer.write(output);
            writer.write(System.getProperty("line.separator"));
            writer.flush();        
        }

    }

    static void cutfileToShell(ArrayList<String> file, OutputStreamWriter writer, String option)  throws IOException{
        ArrayList<Integer> cutnum = optionSplit(option);
        Integer toinf = 0;
        if(cutnum.contains(Integer.MAX_VALUE)){
            toinf = cutnum.get(cutnum.size()-2);
        }
        String output;
        for(String input : file) {
            output = "";
            for (Integer i:cutnum){
                if(i > input.length()){
                    break;
                }
                if(i!=toinf){
                    output = output + String.valueOf(input.charAt(i-1));
                }
                else{
                    output = output + input.substring(i);
                    break;
                }

            }
            writer.write(output);
            writer.write(System.getProperty("line.separator"));
            writer.flush();        
        }

    }



//finish cut

//the methods below used in sort
    static ArrayList<String> read_file(Path file) throws IOException {
        Charset encoding = StandardCharsets.UTF_8;
        BufferedReader reader = Files.newBufferedReader(file,encoding);
        ArrayList<String> listoflines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            listoflines.add(line);
        }
        reader.close();
        return listoflines;
    }

    static void writeToShell(ArrayList<String> listoflines, OutputStreamWriter writer, boolean r) throws IOException {
        listoflines.sort(String::compareTo);
        if (r){
            Collections.reverse(listoflines);
        }
        for(String a:listoflines){
            writer.write(a);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

    static void uniToShell(ArrayList<String> listoflines, OutputStreamWriter writer, boolean i) throws IOException {
        String last=null;
        if (i){
            for(String a:listoflines){
                if (a.equalsIgnoreCase(last)){
                    continue;
                }
                writer.write(a);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                last = a;
            }

        }
        else{
            for(String a:listoflines){
            if (a.equals(last)){
                continue;
            }
            writer.write(a);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
            last = a;
            }
        }    
    }
   
    static ArrayList<String> read_stdin() {
        Scanner in = new Scanner(System.in);
        ArrayList<String> listoflines = new ArrayList<>();
        while(in.hasNext()) {
            listoflines.add(in.nextLine());
        }
        return listoflines;
    }
//finish sort




//the methods below used in find
    static boolean getFiles(File file, String pattern, File path, OutputStreamWriter writer) throws IOException {
        boolean printed1 = false;
        boolean printed2 = false;
        File[] files = file.listFiles();
        for (File a : files) {
            if(a.getName().startsWith(".")){continue;}
            if (matchPattern(a.getName(), pattern)) {
                writer.write(getRelative(path,a));
                writer.write("\n");
                writer.flush();
                printed1 = true;            
            }      
            if(a.isDirectory()){
                if(getFiles(a,pattern,path,writer)){printed2=true;}
              
            }  
        }

        return (printed1 || printed2);
    }

    static boolean matchPattern(String name, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    static String getRelative(File mother, File child){//需要检验
        String rp = Paths.get(mother.getAbsolutePath()).relativize(Paths.get(child.getAbsolutePath())).toString();
        if(rp.startsWith(File.separator)){
            return "."+ rp;
        }
        else{
            return "."+ File.separator + rp;
        }
    }
//finish find


    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("jsh: wrong number of arguments");
                return;
            }
            if (!args[0].equals("-c")) {
                System.out.println("jsh: " + args[0] + ": unexpected argument");
            }
            try {
                eval(args[1], System.out);
            } catch (Exception e) {
                System.out.println("jsh: " + e.getMessage());
            }
        } else {
            Scanner input = new Scanner(System.in);
            try {
                while (true) {
                    String prompt = currentDirectory + "> ";
                    System.out.print(prompt);
                    try {
                        String cmdline = input.nextLine();
                        eval(cmdline, System.out);
                    } catch (Exception e) {
                        System.out.println("jsh: " + e.getMessage());
                    }
                }
            } finally {
                input.close();
            }
        }
    }

}
