package com.aidn5.universalchat.common;

import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.RED;

public class MessageUtil {
    public static ChatComponentText getBackDefaultMessage() {
        ChatComponentText message = new ChatComponentText(
                "Universal chat Channel has been unset!");
        ChatStyle cs = new ChatStyle();
        message.setChatStyle(cs);

        cs.setColor(RED);
        String hover = GRAY + "Using this command will by default fall back to server channels";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hover)));
        return message;
    }

    public static ChatComponentText getErrorSavingSettings() {
        ChatComponentText message = new ChatComponentText(
                "An Error has been encountered saving UniversalChat settings");
        ChatStyle cs = new ChatStyle();
        message.setChatStyle(cs);

        cs.setColor(RED);
        String hover = GRAY + "Check minecraft logs for the full reason";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hover)));
        return message;
    }
}
