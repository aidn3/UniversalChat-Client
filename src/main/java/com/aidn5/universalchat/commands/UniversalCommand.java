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

        if (args[0].equalsIgnoreCase("bridge")) {
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

        } else {
            s.addChatMessage(new ChatComponentText(RED + getCommandUsage(s)));
        }
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
                + GRAY + "This procedure is done to prevent random people from abusing\n"
                + GRAY + "particular permissions.";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));

        return m;
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
