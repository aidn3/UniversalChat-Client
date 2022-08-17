package com.aidn5.universalchat.commands.sub;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.MessageUtil;
import com.aidn5.universalchat.common.Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class IgnoreSubCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "ignore";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "ignore <add/remove/list>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            final String usernameToIgnore;
            try {
                usernameToIgnore = Player.validateUsername(args[1]);
            } catch (Player.NotValidUsername e) {
                sender.addChatMessage(new ChatComponentText(RED + e.getMessage()));
                return;
            }

            for (String ignoredUsername : UniversalChat.configInstance.ignoreList) {
                if (usernameToIgnore.equalsIgnoreCase(ignoredUsername)) {
                    sender.addChatMessage(playerAlreadyInIgnoreList(usernameToIgnore));
                    return;
                }
            }
            UniversalChat.configInstance.ignoreList.add(usernameToIgnore);
            sender.addChatMessage(ignoreListAdd(usernameToIgnore));

            try {
                UniversalChat.configInstance.save();
            } catch (Exception e) {
                e.printStackTrace();
                sender.addChatMessage(MessageUtil.getErrorSavingSettings());
            }

        } else if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            final String usernameToIgnore;
            try {
                usernameToIgnore = Player.validateUsername(args[1]);
            } catch (Player.NotValidUsername e) {
                sender.addChatMessage(new ChatComponentText(RED + e.getMessage()));
                return;
            }

            List<String> ignoreList = UniversalChat.configInstance.ignoreList;
            for (int i = 0; i < ignoreList.size(); i++) {
                String ignoredUsername = ignoreList.get(i);

                if (usernameToIgnore.equalsIgnoreCase(ignoredUsername)) {
                    ignoreList.remove(i);
                    sender.addChatMessage(ignoreListRemove(usernameToIgnore));

                    try {
                        UniversalChat.configInstance.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.addChatMessage(MessageUtil.getErrorSavingSettings());
                    }
                    return;
                }
            }

            sender.addChatMessage(playerNotInTheIgnoreList(usernameToIgnore));

        } else if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            if (UniversalChat.configInstance.ignoreList.isEmpty()) {
                sender.addChatMessage(nobodyIgnored());
                return;
            }

            int page = 1;
            if (args.length >= 2) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (Exception ignored) {
                }
            }

            sender.addChatMessage(displayIgnoreList(page - 1)); // pagination index starts from 0

        } else {
            sender.addChatMessage(new ChatComponentText(RED + getCommandUsage(sender)));
        }
    }

    private ChatComponentText nobodyIgnored() {
        return new ChatComponentText(YELLOW + "You don't have anybody ignored.");
    }

    private ChatComponentText displayIgnoreList(int page) {
        final int PAGE_SIZE = 10;

        List<String> ignoreList = UniversalChat.configInstance.ignoreList;
        int totalPages = ignoreList.size() / PAGE_SIZE;
        if (ignoreList.size() % PAGE_SIZE != 0) totalPages++;

        StringBuilder message = new StringBuilder();
        message.append(AQUA).append("UChat ignored users (page ")
                .append(page + 1).append(" out of ").append(totalPages)
                .append("):");

        page = Math.abs(page);
        int startIndex = Math.min(page * PAGE_SIZE, UniversalChat.configInstance.ignoreList.size());
        int endIndex = Math.min(page * PAGE_SIZE + PAGE_SIZE, UniversalChat.configInstance.ignoreList.size());

        List<String> list = UniversalChat.configInstance.ignoreList.subList(startIndex, endIndex);
        for (int i = 0; i < list.size(); i++) {
            message.append("\n");
            message.append(AQUA).append(startIndex + i + 1).append(". ");
            message.append(YELLOW).append(list.get(i));
        }

        return new ChatComponentText(message.toString());
    }

    private ChatComponentText ignoreListAdd(String player) {
        String message = "Added " + RED + player + RESET + " to the ignore list!";
        return new ChatComponentText(message);
    }

    private ChatComponentText ignoreListRemove(String player) {
        String message = "Removed " + BLUE + player + RESET + " from the ignore list!";
        return new ChatComponentText(message);
    }

    private ChatComponentText playerAlreadyInIgnoreList(String player) {
        String message = RED + player + " is already in the ignore list!";
        return new ChatComponentText(message);
    }

    private ChatComponentText playerNotInTheIgnoreList(String player) {
        String message = RED + player + " is not in the ignore list!";
        return new ChatComponentText(message);
    }
}
