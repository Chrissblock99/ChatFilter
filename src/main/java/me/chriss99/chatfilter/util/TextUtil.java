package me.chriss99.chatfilter.util;

import me.chriss99.chatfilter.data.BannedWords;
import me.chriss99.chatfilter.data.FilterTexts;
import me.chriss99.chatfilter.organize.AllowedWord;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

public class TextUtil {
    public static final String[] lowerCaseABC = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static final String[] upperCaseABC = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static final ArrayList<String> lowerCaseABCList = new ArrayList<>(Arrays.asList(lowerCaseABC));
    public static final ArrayList<String> upperCaseABCList = new ArrayList<>(Arrays.asList(upperCaseABC));
    public static final String[] formattingSymbols = {" ", ".", ",", "?", "!", ":", "\""};
    public static final String[] notAliasedFormattingSymbols = {" ", ".", ",", "?", ":", "\""};

    /**Checks the String for bad Words still allowing legal words containing illegal ones like do-cum-entary to pass
     * this is much more advanced than hasBadWord() as it replaces aliases (like 1 to i)
     * or checks for uppercase letters only etc
     *
     * because of this it is also a lot slower
     * hasBadWord() gets called 8 times and there is a lot of String modification going on
     *
     * NO FILTER IS PERFECT!!
     * AKA do not rely on this
     *
     * @param string The String to check
     * @return If the String contains bad words or not
     */
    public static boolean advancedHasBadWord(@NotNull String string, @NotNull HashMap<String, ArrayList<AllowedWord>> bannedWords,
                                             @NotNull HashMap<String, ArrayList<AllowedWord>> bannedWordsWithNo2SameLettersInSuccession) {

        //Bukkit.getLogger().info("§ball");
        if (hasBadWord(string, bannedWords))
            return true;
        //Bukkit.getLogger().info("§ballABC");
        if (hasBadWord(removeOtherStrings(string.toLowerCase(), lowerCaseABC), bannedWords))
            return true;
        //Bukkit.getLogger().info("§bupperCaseABC");
        if (hasBadWord(removeOtherStrings(string, upperCaseABC), bannedWords))
            return true;
        //Bukkit.getLogger().info("§blowerCaseABC");
        if (hasBadWord(removeOtherStrings(string, lowerCaseABC), bannedWords))
            return true;
        //Bukkit.getLogger().info("§b1");
        if (hasBadWord(removeOtherStrings(replaceAliases(removeStrings(switchCase(string), upperCaseABC)), lowerCaseABC), bannedWords))
            return true;
        //Bukkit.getLogger().info("§b2");
        if (hasBadWord(removeOtherStrings(replaceAliases(removeStrings(string, upperCaseABC)), lowerCaseABC), bannedWords))
            return true;


        //Bukkit.getLogger().info("§bno more then 1 long character islands");
        if (hasBadWord(shortenEveryIslandOfSameCharacterToSetAmountIgnoringCase(string, 1), bannedWordsWithNo2SameLettersInSuccession))
            return true;


        //Bukkit.getLogger().info("§breplacing Aliases: " + string);
        string = removeOtherStrings(replaceAliases(removeStrings(string, notAliasedFormattingSymbols)).toLowerCase(), lowerCaseABC);

        //Bukkit.getLogger().info("§bdeAliased");
        return (hasBadWord(string, bannedWords));
    }

    /**Checks the String for bad Words still allowing legal words containing illegal ones like do-cum-entary to pass
     *
     * @param string The String to check
     * @return If the String contains bad words or not
     */
    public static boolean hasBadWord(@NotNull String string, @NotNull HashMap<String, ArrayList<AllowedWord>> badWords) {
        if (string.isEmpty())
            return false;
        string = string.toLowerCase();

        for (Map.Entry<String, ArrayList<AllowedWord>> filteredWord : badWords.entrySet()) {
            boolean hasMatch = false;
            //Bukkit.getLogger().info("§bFiltering for " + filteredWord.getKey() + ": " + string);
            for(Integer i : getStringOccurrences(string, filteredWord.getKey())) {
                if (string.regionMatches(i, filteredWord.getKey(), 0, filteredWord.getKey().length())) {
                    //Bukkit.getLogger().info("§bMatch found at " + i);
                    for (AllowedWord allowedWord : filteredWord.getValue()) {
                        //Bukkit.getLogger().info("§bChecking if index " + i + " matches " + allowedWord.getWord() + ": " + string);
                        if (string.regionMatches(i - allowedWord.getCharsBefore(), allowedWord.getAllowedWord(), 0, allowedWord.getAllowedWord().length())) {
                            //Bukkit.getLogger().info("§bMatch!");
                            hasMatch = true;
                            break;
                        }// else Bukkit.getLogger().info("§bno match");
                    }
                }
                if (!hasMatch)
                    return true;
            }
        }
        return false;
    }

    /**Replaces all aliases for alphabetic characters inside the String
     *
     * @param string The String to replace aliases
     * @return The deAliased String
     */
    public static @NotNull String replaceAliases(@NotNull String string) {
        for (int i = FilterTexts.aliases.size()-1;i>=0;i--)
            for (Map.Entry<String, String> entry : FilterTexts.aliases.get(i).entrySet()) {
                string = string.replace(entry.getKey(), entry.getValue());
                //Bukkit.getLogger().info("§b" + string);
            }
        return string;
    }

    public static @NotNull String shortenEveryIslandOfSameCharacterToSetAmountIgnoringCase(@NotNull String string, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount to reduce to cannot be smaller then 0!");
        if (amount == 0)
            return "";

        ArrayList<String> charList = new ArrayList<>(Arrays.asList(string.split("")));
        String latestChar = "";
        int charsInSuccession = 1;

        for (int i = charList.size()-1;i>=0;i--) {
            if (latestChar.equalsIgnoreCase(charList.get(i)))
                charsInSuccession++;
            else charsInSuccession = 1;

            if (charsInSuccession>amount)
                charList.remove(i);

            latestChar = charList.get(i);
        }
        return String.join("", charList);
    }

    /**Makes all lowercase letters uppercase and vice-versa
     *
     * @param string The String to switch case of
     * @return The String with switched case
     */
    public static @NotNull String switchCase(@NotNull String string) {
        String[] charList = string.split("");
        for (int i = 0;i< charList.length;i++)
            if (lowerCaseABCList.contains(charList[i])) charList[i] = charList[i].toUpperCase();
            else charList[i] = charList[i].toLowerCase();
        return String.join("", charList);
    }

    /**Removes all strings not contained in the list from the string
     *
     * @param string The string to remove from
     * @param list The strings not to remove
     * @return The filtered String
     */
    public static @NotNull String removeOtherStrings(@NotNull String string, @NotNull String[] list) {
        ArrayList<String> chars = new ArrayList<>(List.of(string.split("")));
        ArrayList<String> notRemove = new ArrayList<>(List.of(list));
        for (int i = chars.size()-1;i>=0;i--)
            if (!notRemove.contains(chars.get(i))) chars.remove(i);
        return String.join("", chars);
    }

    /**Removes all Strings contained in List from String
     *
     * @param string The string to filter
     * @return The filtered String
     */
    public static @NotNull String removeStrings(@NotNull String string, @NotNull String[] list) {
        for (String stringToRemove : list) string = string.replace(stringToRemove, "");
        return string;
    }

    /** Returns a List of all indexes where the next character will be he first of the subString
     *
     * @param mainString The String to check inside
     * @param subString The String to check for
     * @return The List with all starting indexes
     */
    public static @NotNull ArrayList<Integer> getStringOccurrences(@NotNull String mainString, @NotNull String subString) {
        ArrayList<Integer> occurrences = new ArrayList<>();
        for (int index = mainString.indexOf(subString); index >= 0; index = mainString.indexOf(subString, index + 1))
            occurrences.add(index);
        return occurrences;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    static <T> @NotNull T addArrays(@NotNull T array1, @NotNull T array2) {
        if (!array1.getClass().isArray() || !array2.getClass().isArray()) {
            throw new IllegalArgumentException("Only arrays are accepted.");
        }

        Class<?> compType1 = array1.getClass().getComponentType();
        Class<?> compType2 = array2.getClass().getComponentType();

        if (!compType1.equals(compType2)) {
            throw new IllegalArgumentException("Two arrays have different types.");
        }

        int len1 = Array.getLength(array1);
        int len2 = Array.getLength(array2);

        @SuppressWarnings("unchecked")
        //the cast is safe due to the previous checks
        T result = (T) Array.newInstance(compType1, len1 + len2);

        System.arraycopy(array1, 0, result, 0, len1);
        System.arraycopy(array2, 0, result, len1, len2);

        return result;
    }
}
