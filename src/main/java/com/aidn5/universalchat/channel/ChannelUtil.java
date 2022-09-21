package com.aidn5.universalchat.channel;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.Channel;
import com.aidn5.universalchat.common.MessageUtil;
import jline.internal.Nullable;
import net.minecraft.command.ICommandSender;

import java.io.IOException;

public class ChannelUtil {
    static void goBackToDefaultChannel(@Nullable ICommandSender sender) {
        if (Channel.NONE != UniversalChat.configInstance.currentChannel) {
            if (sender != null) sender.addChatMessage(MessageUtil.getBackDefaultMessage());

            UniversalChat.configInstance.currentChannel = Channel.NONE;

            try {
                UniversalChat.configInstance.save();
            } catch (IOException e) {
                e.printStackTrace();
                if (sender != null) sender.addChatMessage(MessageUtil.getErrorSavingSettings());
            }
        }
    }
}
