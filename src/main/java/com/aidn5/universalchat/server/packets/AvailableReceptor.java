
package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.server.SocketHook;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class to redirect client messages to other server
 * instead of using UChat main server to as a communication bridge.
 * <p>
 * If this packet is sent with a non-null different receptor, main server will refuse
 * to process client message packets
 */
/*
 * If main server experience a heavy usage, it will send this packet to some clients,
 * so they can redirect their messages to other server/other-trusted-client to reduce the load.
 */
public class AvailableReceptor implements IPacket {
    /**
     * In-game trusted-client to redirect messages to.
     */
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
