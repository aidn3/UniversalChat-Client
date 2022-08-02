package com.aidn5.universalchat.bridge;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.server.packets.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildBridge {
    private static final Pattern PUBLIC_CHAT = Pattern.compile("^Guild > (?:\\[[A-Z+]{1,10}\\] ){0,3}(\\w{3,32})(?: \\[\\w{1,10}\\]){0,3}:(.{1,256})");
    private static final String BRIDGE_INDICATOR = "⋮";

    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent event) {
        if (event.type != 0) return;
        if (!bridgeEnabled()) return;

        Matcher matcher = PUBLIC_CHAT.matcher(event.message.getUnformattedTextForChat());
        if (!matcher.find()) return;

        String username = matcher.group(1);
        String message = matcher.group(2);

        if (message.startsWith(BRIDGE_INDICATOR)) return;

        String bridgeName = Minecraft.getMinecraft().thePlayer.getName();
        Message packet = new Message(bridgeName, username, message, true);
        UniversalChat.socketHook.sendMessage(packet);
    }

    public static void passServerMessage(Message message) {
        if (!bridgeEnabled()) return;
        if (message.self) return;

        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        if (p != null) {
            String username = message.displayName != null ? message.displayName : message.username;
            String finalMessage = "/gc " + BRIDGE_INDICATOR + username + ": " + message.message;
            System.out.println(finalMessage);
            p.sendChatMessage(finalMessage);
        }
    }

    private static boolean bridgeEnabled() {
        return UniversalChat.configInstance.bridgeEnabled
                && UniversalChat.socketHook.serviceProvider.hasBridgePermission;
    }
}
