package com.aidn5.universalchat.mixins;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.Channel;
import com.aidn5.universalchat.common.MessageUtil;
import com.aidn5.universalchat.server.packets.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.io.IOException;

public class PlayerCallback {
    // return true to exit method, called by asm from #transform
    @SuppressWarnings("unused")
    public static boolean onChatMessage(String message) {
        if (message.startsWith("/")) return false;
        if (UniversalChat.configInstance.currentChannel == Channel.NONE) return false;

        if (!UniversalChat.configInstance.showGlobalChat) {
            UniversalChat.configInstance.currentChannel = Channel.NONE;
            Minecraft.getMinecraft().thePlayer.addChatMessage(MessageUtil.getBackDefaultMessage());

            try {
                UniversalChat.configInstance.save();
            } catch (IOException e) {
                e.printStackTrace();
                Minecraft.getMinecraft().thePlayer.addChatMessage(MessageUtil.getErrorSavingSettings());
            }

            return false;
        }

        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        String displayName = null;
        Message messagePacket = new Message(p.getName(), displayName, message, true);
        UniversalChat.socketHook.sendMessage(messagePacket);
        return true;
    }
}
