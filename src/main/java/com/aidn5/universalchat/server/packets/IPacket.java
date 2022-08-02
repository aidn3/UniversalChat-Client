
package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.server.SocketHook;

import javax.annotation.Nonnull;

public interface IPacket {
    void processPacket(@Nonnull SocketHook socketHook);
}
