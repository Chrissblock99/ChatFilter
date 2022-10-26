package me.chriss99.chatfilter.organize;

import org.junit.Assert;
import org.junit.Test;

public class AllowedWordTest {

    @Test
    public void document() {
        AllowedWord correct = new AllowedWord("document", 2, 3, 3);
        AllowedWord test = new AllowedWord("document", "cum");

        Assert.assertEquals(correct, test);
    }

    @Test
    public void grape() {
        AllowedWord correct = new AllowedWord("grape", 1, 4, 0);
        AllowedWord test = new AllowedWord("grape", "rape");

        Assert.assertEquals(correct, test);
    }

    @Test
    public void night() {
        AllowedWord correct = new AllowedWord("night", 0, 3, 2);
        AllowedWord test = new AllowedWord("night", "nig");

        Assert.assertEquals(correct, test);
    }
}