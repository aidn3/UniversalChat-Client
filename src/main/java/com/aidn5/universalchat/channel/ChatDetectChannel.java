package com.aidn5.universalchat.channel;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatDetectChannel {

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().startsWith("Opened a chat conversation with")) {
            ChannelUtil.goBackToDefaultChannel(Minecraft.getMinecraft().thePlayer);
        }
    }
}
