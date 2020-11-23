package uk.ac.ucl.jsh.toolkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    public static boolean matchPattern(String input, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean findPattern(String input, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        return m.find();
    }
    

    
}