
package com.aidn5.universalchat.commands;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.Channel;
import com.aidn5.universalchat.common.MessageUtil;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import java.io.IOException;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class ChatCommand extends CommandBase {
    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 0) {
            ChatComponentText m = new ChatComponentText(getCommandUsage(s));
            m.setChatStyle(new ChatStyle().setColor(RED));
            s.addChatMessage(m);
            return;
        }

        Channel channel = Channel.getChannel(args[0]);
        if (channel == Channel.NONE) {
            if (channel != UniversalChat.configInstance.currentChannel) {
                s.addChatMessage(MessageUtil.getBackDefaultMessage());
            }

            Minecraft.getMinecraft().thePlayer.sendChatMessage("/ch " + String.join(" ", args));

        } else if (channel == UniversalChat.configInstance.currentChannel) {
            s.addChatMessage(getAlreadyOnChannel());

        } else {
            s.addChatMessage(getChannelChanged(channel));
        }

        UniversalChat.configInstance.currentChannel = channel;

        try {
            UniversalChat.configInstance.save();
        } catch (IOException e) {
            e.printStackTrace();
            s.addChatMessage(MessageUtil.getErrorSavingSettings());
        }
    }

    private ChatComponentText getChannelChanged(Channel channel) {
        String message = GREEN + "You are now in the "
                + GOLD + channel.displayName() + GREEN + " channel";
        return new ChatComponentText(message);
    }

    private ChatComponentText getAlreadyOnChannel() {
        String message = RED + "You are already in this channel!";
        return new ChatComponentText(message);
    }

    @Override
    public String getCommandName() {
        return "ch";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Valid usage: \"/ch universal\" for universal channel. \n"
                + "Otherwise, usage exactly as Hypixel.\n"
                + "Note: trying to change the channel will reset the behaviour to the default one.";
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.asList("chat", new String[0]);
    }
}
