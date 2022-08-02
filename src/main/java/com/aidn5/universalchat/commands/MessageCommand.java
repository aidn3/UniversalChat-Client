
package com.aidn5.universalchat.commands;


import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.MessageUtil;
import com.aidn5.universalchat.server.packets.Message;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class MessageCommand extends CommandBase {

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 0) {
            s.addChatMessage(new ChatComponentText(RED + getCommandUsage(s)));
            return;
        }

        if (args[0].equals("toggle")) {
            boolean status = UniversalChat.configInstance.showGlobalChat = !UniversalChat.configInstance.showGlobalChat;
            String localizedStatus = status ? GREEN + "enabled" : RED + "disabled";
            String msg = String.format("Universal chat is %s", localizedStatus);
            s.addChatMessage(new ChatComponentText(msg));

            try {
                UniversalChat.configInstance.save();
            } catch (IOException e) {
                e.printStackTrace();
                s.addChatMessage(MessageUtil.getErrorSavingSettings());
            }

        } else if (!UniversalChat.configInstance.showGlobalChat) {
            String msg = RED + "Do " + DARK_AQUA + "/uc toggle " + RED + "to enable Universal chat first!";
            s.addChatMessage(new ChatComponentText(msg));

        } else {
            String displayName = null;
            boolean self = true;
            Message message = new Message(s.getName(), displayName, String.join(" ", args), self);
            UniversalChat.socketHook.sendMessage(message);
        }
    }

    @Override
    public String getCommandName() {
        return "uc";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/uc Hi there, I'm a fellow player!";
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.asList("universalchat", new String[0]);

    }
}
