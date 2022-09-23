package com.aidn5.universalchat.bridge;

import com.aidn5.universalchat.MessageDisplay;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildChatCompactor {
    // Guild > [MVP+] bridge: .displayUsername: hello world
    private static final Pattern PUBLIC_CHAT = Pattern.compile("^Guild > (?:\\[[A-Z+]{1,10}\\] ){0,3}(\\w{3,32})(?: \\[\\w{1,10}\\]){0,3}: \\.(\\w{3,32}): (.{1,256})");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type != 0) return;

        String chatMessage = ChatFormatting.stripFormatting(event.message.getFormattedText());
        Matcher matcher = PUBLIC_CHAT.matcher(chatMessage);
        if (!matcher.find()) return;

        String senderName = matcher.group(1).trim();
        String displayName = matcher.group(2).trim();
        String sentMessage = matcher.group(3).trim();

        IChatComponent editedGuildMessage = new ChatComponentText("")
                .appendText(ChatFormatting.DARK_GREEN + "Guild > ")
                .appendText(ChatFormatting.GOLD + "[UGB] ")
                .appendSibling(MessageDisplay.displayName(senderName, displayName))
                .appendText(": ");

        MessageDisplay.formatMessage(sentMessage).forEach(editedGuildMessage::appendSibling);

        MessageDisplay.display(editedGuildMessage);
        event.setCanceled(true);
    }
}
