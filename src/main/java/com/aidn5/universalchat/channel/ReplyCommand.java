package com.aidn5.universalchat.channel;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.List;


public class ReplyCommand extends CommandBase {
    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 0) {
            ChannelUtil.goBackToDefaultChannel(s);
        }

        Minecraft.getMinecraft().thePlayer.sendChatMessage("/r " + String.join(" ", args));
    }

    @Override
    public String getCommandName() {
        return "r";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.asList("reply", new String[0]);
    }
}
