package com.aidn5.universalchat.commands.sub;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.MessageUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.io.IOException;

import static net.minecraft.util.EnumChatFormatting.*;

public class BridgeSubCommand extends ISubCommand {
    @Override
    public String getCommandName() {
        return "bridge";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        boolean allowed = UniversalChat.socketHook.serviceProvider.hasBridgePermission;

        if (allowed) {
            UniversalChat.configInstance.bridgeEnabled = !UniversalChat.configInstance.bridgeEnabled;

            sender.addChatMessage(UniversalChat.configInstance.bridgeEnabled
                    ? getEnableBridge()
                    : getDisableBridge());

        } else {
            sender.addChatMessage(getBridgeNotAllowed());

            if (UniversalChat.configInstance.bridgeEnabled) {
                sender.addChatMessage(getForceDisableBridge());
            }

            UniversalChat.configInstance.bridgeEnabled = false;
        }

        try {
            UniversalChat.configInstance.save();
        } catch (IOException e) {
            e.printStackTrace();
            sender.addChatMessage(MessageUtil.getErrorSavingSettings());
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
                + DARK_GRAY + "This procedure is done to prevent\n"
                + DARK_GRAY + "random people from abusing particular permissions.";
        cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverText)));

        return m;
    }

    private ChatComponentText getForceDisableBridge() {
        String message = RED + "Bridge-mode was enabled. Disabling it...";
        return new ChatComponentText(message);
    }

    @Override
    public String getCommandDescription() {
        return "Convert the client into GuildBridge linking guild chat with UChat service.";
    }
}
