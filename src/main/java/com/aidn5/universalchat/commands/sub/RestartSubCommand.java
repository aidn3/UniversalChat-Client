package com.aidn5.universalchat.commands.sub;

import com.aidn5.universalchat.UniversalChat;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import static net.minecraft.util.EnumChatFormatting.*;

public class RestartSubCommand extends ISubCommand {
    @Override
    public String getCommandName() {
        return "restart";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(getRestartSocket());
        UniversalChat.socketHook.restartConnection();
    }

    private static IChatComponent getRestartSocket() {
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

    @Override
    public String getCommandDescription() {
        return "Restart connection to UChat service. Helps sometimes if can't see UChat messages.";
    }
}
