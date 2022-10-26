package me.chriss99.chatfilter.data;

import me.chriss99.chatfilter.ChatFilter;
import me.chriss99.chatfilter.organize.AllowedWord;
import me.chriss99.chatfilter.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BannedWords {
    private static HashMap<String, ArrayList<AllowedWord>> bannedWords = null;
    private static HashMap<String, ArrayList<AllowedWord>> bannedWordsWithNo2SameLettersInSuccession = null;

    public static void initializeBannedWords() {
        ConfigurationSection bannedWordsConfigSection = ChatFilter.getBannedWordsConfigManager().getConfig().getConfigurationSection("words");
        if (bannedWordsConfigSection == null) {
            Bukkit.getLogger().warning("No \"words:\" section in bannedWords.yml!");
            return;
        }

        BannedWords.bannedWords = new HashMap<>();
        ArrayList<AllowedWord> allowedWordList = new ArrayList<>();

        Bukkit.getLogger().info(ChatFilter.getBannedWordsConfigManager().getConfig().getString("words"));
        //for (Map.Entry<String, Object> mapEntry : ChatFilter.getBannedWordsConfigManager().getConfig().getValues(true).entrySet())
        //   Bukkit.getLogger().info(mapEntry.getKey() + " | " + mapEntry.getValue());

        /*for (String bannedWord : bannedWordsConfigSection.getKeys(false)) {
            Bukkit.getLogger().info(bannedWord);
            for (String allowedWord : bannedWordsConfigSection.getStringList(bannedWord)) {
                Bukkit.getLogger().info(" -" + allowedWord);
                allowedWordList.add(new AllowedWord(allowedWord, bannedWord));
            }

            //noinspection unchecked
            BannedWords.bannedWords.put(bannedWord, (ArrayList<AllowedWord>) allowedWordList.clone());
            allowedWordList.clear();
        }*/
    }

    public static void initializeBannedWordsWithNo2SameLettersInSuccession() {
        bannedWordsWithNo2SameLettersInSuccession = new HashMap<>();
        for (String bannedWord : getBannedWords().keySet()) {         //doing getBannedWords here so bannedWords is initialised if not present
            ArrayList<AllowedWord> allowedWords = new ArrayList<>();

            for (AllowedWord allowedWord : bannedWords.get(bannedWord))
                allowedWords.add(new AllowedWord(TextUtil.shortenEveryIslandOfSameCharacterToSetAmountIgnoringCase(allowedWord.getAllowedWord(), 1),
                        TextUtil.shortenEveryIslandOfSameCharacterToSetAmountIgnoringCase(allowedWord.getBannedWord(), 1)));

            bannedWordsWithNo2SameLettersInSuccession.put(bannedWord, allowedWords);
        }
    }

    public static HashMap<String, ArrayList<AllowedWord>> getBannedWords() {
        if (bannedWords == null)
            initializeBannedWords();

        return bannedWords;
    }

    public static HashMap<String, ArrayList<AllowedWord>> getBannedWordsWithNo2SameLettersInSuccession() {
        if (bannedWordsWithNo2SameLettersInSuccession == null)
            initializeBannedWordsWithNo2SameLettersInSuccession();

        return bannedWordsWithNo2SameLettersInSuccession;
    }
}
