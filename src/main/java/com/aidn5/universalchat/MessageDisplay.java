
package com.aidn5.universalchat;

import com.aidn5.universalchat.server.packets.Broadcast;
import com.aidn5.universalchat.server.packets.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.util.EnumChatFormatting.*;

public class MessageDisplay {
    private static ChatComponentText chatPrefix() {
        ChatComponentText m = new ChatComponentText(DARK_AQUA + "UChat > ");
        ChatStyle cs = new ChatStyle();
        m.setChatStyle(cs);

        String hoverText = DARK_AQUA + BOLD.toString() + "UniversalChat\n"
                + "You can do '/chat universal' to change channel.\n"
                + "Or talk directly by doing '/uc hello there!'";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));

        return m;
    }

    private static ChatComponentText displayName(Message event) {
        if (event.displayName == null || event.displayName.isEmpty()) {
            return new ChatComponentText(GOLD + event.username);
        }

        ChatComponentText m = new ChatComponentText(GOLD + "." + event.displayName);
        ChatStyle cs = new ChatStyle();
        m.setChatStyle(cs);

        String hoverText = event.username + " is sending this message.\n"
                + DARK_GRAY + "This message is mostly sent from a guild-bridge";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));

        return m;
    }

    // https://superuser.com/a/623174
    private final static Pattern LINK_REGEX = Pattern.compile("(https?):\\/\\/[a-z0-9\\.:].*?(?=\\s)");

    private static ArrayList<ChatComponentText> formatMessage(String message) {
        /*
          Due to a bug in regex pattern LINK_REGEX,
          link isn't detected if it is at the end of a message
          unless it is followed by a terminator.
          A workaround is done here to avoid complicating the regex any further.
         */
        message += ' ';

        ArrayList<ChatComponentText> comp = new ArrayList<>();

        Matcher matcher = LINK_REGEX.matcher(message);
        int index = 0;
        while (matcher.find()) {
            /*
            Whitespace char is added after every match
            to prevent "Situationlikethis" and "make it split"
             */
            comp.add(new ChatComponentText(message.substring(index, matcher.start())));

            String link = matcher.group();
            System.out.println("LINK: " + link);
            ChatComponentText linkComp = new ChatComponentText(link);
            ChatStyle cs = new ChatStyle();
            linkComp.setChatStyle(cs);

            cs.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));

            String hoverText = "Opens " + YELLOW + link + RESET + " on click.";
            cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));
            comp.add(linkComp);

            index = matcher.end();
        }

        if (index != message.length()) {
            comp.add(new ChatComponentText(message.substring(index) + ' '));
        }

        return comp;
    }

    public static void showGlobalMessage(Message event) {
        if (!UniversalChat.configInstance.showGlobalChat) return;
        IChatComponent displayMessage = new ChatComponentText("")
                .appendSibling(chatPrefix())
                .appendSibling(displayName(event))
                .appendText(": ");

        formatMessage(event.message).forEach(displayMessage::appendSibling);

        display(displayMessage);
    }

    public static void showBroadcastMessage(Broadcast broadcast) {
        if (!UniversalChat.configInstance.showBroadcast && !broadcast.important) return;

        IChatComponent displayMessage = new ChatComponentText("")
                .appendSibling(chatPrefix());

        formatMessage(broadcast.message).forEach(displayMessage::appendSibling);

        display(displayMessage);
    }

    private static void display(IChatComponent component) {
        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        if (p != null) {
            p.addChatComponentMessage(component);
        }
    }
}
