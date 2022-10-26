package me.chriss99.chatfilter.util;

import me.chriss99.chatfilter.organize.AllowedWord;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TextUtilTest {
    HashMap<String, ArrayList<AllowedWord>> bannedWords;
    HashMap<String, ArrayList<AllowedWord>> bannedWordsWithNo2SameLettersInSuccession;

    @Test
    public void grapePassesAdvancedHasBadWord() {
        Assert.assertFalse(TextUtil.advancedHasBadWord("grape", bannedWords, bannedWordsWithNo2SameLettersInSuccession));
    }
}
