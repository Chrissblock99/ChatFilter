package me.chriss99.chatfilter.util;

import me.chriss99.chatfilter.ChatFilter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventUtil {
    public static void register(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, ChatFilter.getInstance());
    }

}
