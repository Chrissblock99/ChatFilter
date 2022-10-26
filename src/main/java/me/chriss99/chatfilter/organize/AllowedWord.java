package me.chriss99.chatfilter.organize;

import me.chriss99.chatfilter.util.TextUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllowedWord {
    private final String allowedWord;
    private final String bannedWord;
    private final int charsBefore;
    private final int charsMid;
    private final int charsAfter;

    /** Creates an AllowedWord Object
     *
     * @param allowedWord The allowed word
     * @param charsBefore The number of characters before the banned word starts
     * @param charsMid The length of the banned word
     * @param charsAfter The amount of characters in the word after the banned word ends
     */
    public AllowedWord(@NotNull String allowedWord, int charsBefore, int charsMid, int charsAfter) {
        if (allowedWord.length() != (charsBefore+charsMid+charsAfter))
            throw new IllegalArgumentException("The length specifications have to match the words length!");

        this.allowedWord = allowedWord;
        this.bannedWord = allowedWord.substring(charsBefore, charsBefore+charsAfter);
        this.charsBefore = charsBefore;
        this.charsMid = charsMid;
        this.charsAfter = charsAfter;
    }

    /** Creates an AllowedWord Object
     *
     * @param allowedWord The allowed word
     * @param bannedWord The banned Word
     */
    public AllowedWord(@NotNull String allowedWord, @NotNull String bannedWord) {
        if (!allowedWord.contains(bannedWord))
            throw new IllegalArgumentException("The banned word has to be contained in the allowed word!");

        this.allowedWord = allowedWord;
        this.bannedWord = bannedWord;
        this.charsBefore = TextUtil.getStringOccurrences(allowedWord, bannedWord).get(0);
        this.charsMid = bannedWord.length();
        this.charsAfter = allowedWord.length()-(charsBefore+charsMid);
    }

    @Override
    public String toString() {
        return "AllowedWord=(allowedWord=" + allowedWord +
                ",bannedWord=" + bannedWord +
                ",charsBefore=" + charsBefore
                + ",charsMid=" + charsMid +
                ",charsAfter=" + charsAfter + ")";
    }

    public String getAllowedWord() {return allowedWord;}

    public String getBannedWord() {return bannedWord;}

    public int getCharsBefore() {return charsBefore;}

    public int getCharsMid() {return charsMid;}

    public int getCharsAfter() {return charsAfter;}
}
