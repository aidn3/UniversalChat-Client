package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.server.SocketHook;

import javax.annotation.Nonnull;

/**
 * Permissions the client has.
 * All permissions are denied by default
 */
public class Role implements IPacket {
    /**
     * Whether client can become a chat bridge (for guild, etc)
     * and be a middle-man between vanilla users and UChat servers.
     * <p>
     * This permission allows the client to change its nickname
     * and send messages on behalf of others.
     * The final received messages by others will contain
     * the original client name + a display name to represent original author user
     */
    public final boolean hasBridgePermission;

    public Role(boolean hasBridgePermission) {
        this.hasBridgePermission = hasBridgePermission;
    }

    @Override
    public void processPacket(@Nonnull SocketHook socketHook) {
        socketHook.serviceProvider.hasBridgePermission = this.hasBridgePermission;
    }
}
