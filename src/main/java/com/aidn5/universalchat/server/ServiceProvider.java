
package com.aidn5.universalchat.server;

import com.aidn5.universalchat.server.packets.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ServiceProvider {
    @Nonnull
    private final SocketHook socketHook;

    @Nullable
    private String receptor;

    public boolean hasBridgePermission = false;

    public ServiceProvider(@Nonnull SocketHook socketHook) {
        this.socketHook = Objects.requireNonNull(socketHook);
    }

    public void updateReceptor(@Nullable String receptor) {
        this.receptor = receptor;
    }

    public void sendChat(@Nonnull Message packet) {
        if (receptor == null || receptor.isEmpty()) {
            socketHook.sendPacket(packet);
        } else {

            String command = String.format("/msg %s %s:%s", receptor, packet.username, packet.message);
            System.out.println(command);

            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if (player != null) player.sendChatMessage(command);
        }
    }
}
