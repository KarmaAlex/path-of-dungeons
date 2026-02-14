package it.univaq.pathofdungeons.utils;

public class StringUtils {

    private StringUtils(){}

    /**
     * Capitalize a given string
     * @param s string to capitalize
     * @return a string with the first character capitalized and all others in lowercase
     */
    public static String capitalize(String s){
        return s.substring(0,1).toUpperCase().concat(s.substring(1).toLowerCase());
    }

    /**
     * Capitalized a string after splitting on "_"
     * @param s string to capitalize
     * @return the original string, capitalized, and with "_" removed and replaced by a space
     */
    public static String capitalizeSplit(String s){
        String[] strings = s.split("_");
        String out = StringUtils.capitalize(strings[0]);
        for(int i = 1; i < strings.length; i++){
            out = out.concat(" "+ strings[i].toLowerCase());
        }
        return out;
    }
}
