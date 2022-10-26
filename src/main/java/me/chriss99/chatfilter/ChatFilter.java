package me.chriss99.chatfilter;

import me.chriss99.chatfilter.data.BannedWords;
import me.chriss99.chatfilter.events.AsyncChat;
import me.chriss99.chatfilter.util.ConfigFileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class ChatFilter extends JavaPlugin {
    private static ChatFilter instance;
    private static ConfigFileManager bannedWordsConfigManager;

    @Override
    public void onEnable() {
        instance = this;
        bannedWordsConfigManager = new ConfigFileManager(this, "bannedWords");

        BannedWords.getBannedWords();

        new AsyncChat();

        Bukkit.getLogger().info("ChatFilter started up.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("ChatFilter shut down.");
    }

    public static @NotNull ChatFilter getInstance() {
        return instance;
    }

    public static @NotNull ConfigFileManager getBannedWordsConfigManager() {
        return bannedWordsConfigManager;
    }
}
