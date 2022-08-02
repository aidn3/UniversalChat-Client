
package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.server.SocketHook;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AvailableReceptor implements IPacket {
    @Nullable
    public final String receptor;

    public AvailableReceptor(@Nullable String receptor) {
        this.receptor = Objects.requireNonNull(receptor);
    }

    @Override
    public void processPacket(@Nonnull SocketHook socketHook) {
        socketHook.serviceProvider.updateReceptor(this.receptor);
    }
}
