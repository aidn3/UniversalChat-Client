package com.aidn5.universalchat.commands.sub;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.MessageUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;
import static net.minecraft.util.EnumChatFormatting.RED;

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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            String usernameToIgnore = args[1]; //TODO: add method to sanitize usernames

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
            String usernameToIgnore = args[1]; //TODO: add method to sanitize usernames

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

            sender.addChatMessage(displayIgnoreList(page));
        }
    }

    private ChatComponentText nobodyIgnored() {
        return new ChatComponentText(YELLOW + "You don't have anybody ignored.");
    }

    private ChatComponentText displayIgnoreList(int page) {
        final int PAGE_SIZE = 10;

        int totalPages = UniversalChat.configInstance.ignoreList.size() % PAGE_SIZE;
        StringBuilder message = new StringBuilder("UChat ignored users (page " + page + " out of " + totalPages + "):\n");

        page = Math.abs(page);
        int startIndex = Math.min(page * PAGE_SIZE, UniversalChat.configInstance.ignoreList.size());
        int endIndex = Math.min(page * PAGE_SIZE + PAGE_SIZE, UniversalChat.configInstance.ignoreList.size());

        List<String> list = UniversalChat.configInstance.ignoreList.subList(startIndex, endIndex);
        for (int i = page * PAGE_SIZE; i < list.size(); i++) {
            message
                    .append(AQUA).append(startIndex + i).append(". ")
                    .append(YELLOW).append(list.get(i)).append("\n");
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