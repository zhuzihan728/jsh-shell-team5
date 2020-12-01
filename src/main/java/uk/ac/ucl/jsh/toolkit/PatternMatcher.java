package uk.ac.ucl.jsh.toolkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PatternMatcher deals with matching strings with certain patterns
 */
public class PatternMatcher {

    /**
     * a method that takes a string and a pattern, checking if the former matches
     * the later
     * 
     * @param input   The string to be examined
     * @param pattern The regex pattern which the string should be matching
     * @return boolean It holds true if the string matches the pattern, false
     *         otherwise
     */
    public static boolean matchPattern(String input, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    /**
     * a method that takes a string and a pattern, checking if any substring of
     * former matches the later
     * 
     * @param input   The string to be examined
     * @param pattern The regex pattern for examining the string
     * @return boolean It holds true if any substring of the string matches the
     *         pattern, false otherwise
     */
    public static boolean findPattern(String input, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        return m.find();
    }

}