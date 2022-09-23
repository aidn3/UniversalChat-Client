package com.aidn5.universalchat.commands;

import com.aidn5.universalchat.commands.sub.BridgeSubCommand;
import com.aidn5.universalchat.commands.sub.ISubCommand;
import com.aidn5.universalchat.commands.sub.IgnoreSubCommand;
import com.aidn5.universalchat.commands.sub.RestartSubCommand;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.minecraft.util.EnumChatFormatting.RED;

public class UniversalCommand extends CommandBase {

    private final ISubCommand[] subCommands = new ISubCommand[]{
            new IgnoreSubCommand(),
            new RestartSubCommand(),
            new BridgeSubCommand()
    };

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(RED + getCommandUsage(sender)));
            return;

        } else if (args[0].equalsIgnoreCase("help")) {
            sender.addChatMessage(getDetailedHelp(sender));
            return;
        }

        String commandName = args[0];
        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
        Optional<ISubCommand> command = Arrays.stream(subCommands)
                .filter(c -> c.getCommandName().equalsIgnoreCase(commandName))
                .findFirst();

        if (command.isPresent()) {
            command.get().processCommand(sender, subCommandArgs);

        } else {
            sender.addChatMessage(new ChatComponentText(RED + getCommandUsage(sender)));
        }
    }

    private ChatComponentText getDetailedHelp(ICommandSender sender) {
        ChatComponentText helpMessage = new ChatComponentText("");

        helpMessage.appendText(ChatFormatting.RED
                + ChatFormatting.BOLD.toString()
                + "UniversalChat help page:");

        for (ISubCommand command : subCommands) {
            helpMessage.appendText("\n");

            IChatComponent subHelp = new ChatComponentText("")
                    .appendText(ChatFormatting.RED + "/" + getCommandName()
                            + " " + command.getCommandName()
                            + " " + command.getCommandUsage(sender));

            ChatComponentText hoverMessage = new ChatComponentText(command.getCommandDescription());
            subHelp.getChatStyle()
                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMessage))
                    .setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                            "/" + getCommandName() + " " + command.getCommandName() + " "));

            helpMessage.appendSibling(subHelp);
        }

        return helpMessage;
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.asList("universal", new String[]{"uchat"});
    }

    @Override
    public String getCommandName() {
        return "u";
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
