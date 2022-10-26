package me.chriss99.chatfilter.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.chriss99.chatfilter.data.BannedWords;
import me.chriss99.chatfilter.data.FilterTexts;
import me.chriss99.chatfilter.util.MathUtil;
import me.chriss99.chatfilter.util.TextUtil;
import me.chriss99.chatfilter.util.EventUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class AsyncChat implements Listener {
    //int chatCooldown = ChatFilter.getFileConfiguration().getInt("chat-cooldown");

    public AsyncChat() {
        EventUtil.register(this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncChatEvent event) {
        //Bukkit.getLogger().info("" + chatCooldown);
        if (event.message() instanceof TextComponent textComponent)
            if (TextUtil.advancedHasBadWord(textComponent.toString(), BannedWords.getBannedWords(), BannedWords.getBannedWordsWithNo2SameLettersInSuccession()))
                event.message(Component.text((String) MathUtil.randomEntry(FilterTexts.funSentence)));
    }
}
