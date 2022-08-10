package com.aidn5.universalchat.commands;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.MessageUtil;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class UniversalCommand extends CommandBase {

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 0) {
            s.addChatMessage(new ChatComponentText(RED + getCommandUsage(s)));
            return;
        }

        if (args[0].equalsIgnoreCase("restart")) {
            s.addChatMessage(getRestartSocket());
            UniversalChat.socketHook.restartConnection();

        } else if (args[0].equalsIgnoreCase("bridge")) {
            boolean allowed = UniversalChat.socketHook.serviceProvider.hasBridgePermission;

            if (allowed) {
                UniversalChat.configInstance.bridgeEnabled = !UniversalChat.configInstance.bridgeEnabled;

                s.addChatMessage(UniversalChat.configInstance.bridgeEnabled
                        ? getEnableBridge()
                        : getDisableBridge());

            } else {
                s.addChatMessage(getBridgeNotAllowed());

                if (UniversalChat.configInstance.bridgeEnabled) {
                    s.addChatMessage(getForceDisableBridge());
                }

                UniversalChat.configInstance.bridgeEnabled = false;
            }

            try {
                UniversalChat.configInstance.save();
            } catch (IOException e) {
                e.printStackTrace();
                s.addChatMessage(MessageUtil.getErrorSavingSettings());
            }

        }

        else if (args[0].equalsIgnoreCase("ignore") && args[1] != null) {
            if (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove") && !args[1].equalsIgnoreCase("list"))
                return;

            if (args[1].equalsIgnoreCase("add")) {
                if (args[2].length() < 2 || args[2].length() > 16) return;

                for (int i = 0; i < UniversalChat.configInstance.ignoreList.size(); i++) {
                    if (args[2].equalsIgnoreCase(UniversalChat.configInstance.ignoreList.get(i))) {
                        s.addChatMessage(playerAlreadyInIgnoreList(args[2]));
                        return;
                    }
                }

                try {
                    System.out.println(UniversalChat.configInstance.ignoreList);
                    UniversalChat.configInstance.ignoreList.add(args[2]);
                    UniversalChat.configInstance.save();
                    s.addChatMessage(ignoreListAdd(args[2]));
                } catch (Exception e) {
                    e.printStackTrace();
                    s.addChatMessage(MessageUtil.getErrorSavingSettings());
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (args[2].length() < 2 || args[2].length() > 16) return;

                boolean playerInTheIgnoreList = false;
                for (int i = 0; i < UniversalChat.configInstance.ignoreList.size(); i++) {
                    if (!args[2].equalsIgnoreCase(UniversalChat.configInstance.ignoreList.get(i))) {
                        playerInTheIgnoreList = false;
                    } else {
                        playerInTheIgnoreList = true;
                        break;
                    }
                }

                if (!playerInTheIgnoreList) {
                    s.addChatMessage(playerNotInTheIgnoreList(args[2]));
                    return;
                }

                try {
                    for (int i = 0; i < UniversalChat.configInstance.ignoreList.size(); i++) {
                        if (args[2].equalsIgnoreCase(UniversalChat.configInstance.ignoreList.get(i))) {
                            UniversalChat.configInstance.ignoreList.remove(i);
                            break;
                        }
                    }
                    UniversalChat.configInstance.save();
                    s.addChatMessage(ignoreListRemove(args[2]));
                } catch (Exception e) {
                    e.printStackTrace();
                    s.addChatMessage(MessageUtil.getErrorSavingSettings());
                }
            }
            else if (args[1].equalsIgnoreCase("list")) {
                s.addChatMessage(displayIgnoreList());
            }
        }
        else {
            s.addChatMessage(new ChatComponentText(RED + getCommandUsage(s)));
        }
    }

    private IChatComponent getRestartSocket() {
        ChatComponentText m = new ChatComponentText(
                DARK_AQUA + "UniversalChat"
                        + RESET + " is restarting..."
                        + GRAY + " (hover here)");
        ChatStyle cs = new ChatStyle();
        m.setChatStyle(cs);

        String hoverText = DARK_AQUA + BOLD.toString() + "There are multiple reasons for unresponsiveness:\n"
                + "1. UniversalChat servers are updating and new features are coming\n"
                + "2. A slow internet connection is preventing from connecting to servers\n"
                + "3. Firewall or antivirus is preventing UniversalChat from working\n"
                + "4. Other mods are conflicted with UniversalChat\n\n"
                + DARK_GRAY + "If error is not fixed or is re-encountered multiple times, \n"
                + DARK_GRAY + "please contact admins on the official discord server or in-game via `/msg aidn5`";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));

        return m;
    }

    private IChatComponent getEnableBridge() {
        ChatComponentText m = new ChatComponentText(
                DARK_AQUA + "UniversalChat"
                        + RESET + " GuildBridge-Mode has been " + GREEN + "enabled" + RESET + "!"
                        + GRAY + " (hover here)");
        ChatStyle cs = new ChatStyle();
        m.setChatStyle(cs);

        String hoverText = DARK_AQUA + BOLD.toString() + "UniversalChat GuildBridge-Mode\n"
                + "On this mode, all guild chats are shared with UniversalChat service\n"
                + "linking everyone together!\n\n"
                + RED + BOLD + "As the owner of the bridge,\n"
                + RED + BOLD + "make sure to moderate your guild and prevent abuse from occurring.\n"
                + RED + BOLD + "Failing in fulfilling this role may result in ban.";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));

        return m;
    }

    private ChatComponentText getDisableBridge() {
        return new ChatComponentText("Universal chat's GuildBridge-Mode has been " + RED + "disabled");
    }

    private ChatComponentText getBridgeNotAllowed() {
        ChatComponentText m = new ChatComponentText(
                DARK_AQUA + "UniversalChat"
                        + RED + " GuildBridge-Mode requires a permission from UniversalChat admins!"
                        + GRAY + " (hover here)");
        ChatStyle cs = new ChatStyle();
        m.setChatStyle(cs);

        String hoverText = DARK_AQUA + BOLD.toString() + "UniversalChat GuildBridge-Mode\n"
                + "Contact UniversalChat Staff or Admins\n"
                + "by either opening a ticket  on the official discord server\n"
                + "or by messaging them directly on UniversalChat service.\n\n"
                + "Note: Guild's Staff or Guild-Master must do the procedure.\n\n"
                + DARK_GRAY + "This procedure is done to prevent\n"
                + DARK_GRAY + "random people from abusing particular permissions.";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));

        return m;
    }

    private ChatComponentText displayIgnoreList() {
        String message;
        if (UniversalChat.configInstance.ignoreList.size() == 0) {
            return new ChatComponentText("You don't have anybody ignored.");
        }

        message = "Your ignore list consists of: \n[";
        for (int i = 0; i < UniversalChat.configInstance.ignoreList.size(); i++) {
            if (i + 1 == UniversalChat.configInstance.ignoreList.size()) {
                message += GRAY + UniversalChat.configInstance.ignoreList.get(i) + RESET + "]";
                return new ChatComponentText(message);
            } else {
                message += GRAY + UniversalChat.configInstance.ignoreList.get(i) + ", ";
            }
        }

        return new ChatComponentText(message);
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

    private ChatComponentText getForceDisableBridge() {
        String message = RED + "Bridge-mode was enabled. Disabling it...";
        return new ChatComponentText(message);
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.asList("u", new String[0]);
    }

    @Override
    public String getCommandName() {
        return "universal";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/u help";
    }
}
