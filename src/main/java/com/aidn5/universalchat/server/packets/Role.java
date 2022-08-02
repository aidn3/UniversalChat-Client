package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.server.SocketHook;

import javax.annotation.Nonnull;

public class Role implements IPacket {
    public final boolean hasBridgePermission;

    public Role(boolean hasBridgePermission) {
        this.hasBridgePermission = hasBridgePermission;
    }

    @Override
    public void processPacket(@Nonnull SocketHook socketHook) {
        socketHook.serviceProvider.hasBridgePermission = this.hasBridgePermission;
    }
}
