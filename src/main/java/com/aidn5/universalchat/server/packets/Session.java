
package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.server.SocketHook;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Session packet is a private key created by the server.
 * This key can be used to re-authorize without having to require another Mojang
 * authorization. It is used to be more independent of Mojang servers.
 *
 * @author aidn5
 */
public class Session implements IPacket {
    @Nullable
    public final String key;

    public Session(@Nullable String key) {
        this.key = Objects.requireNonNull(key);
    }

    @Override
    public void processPacket(@Nonnull SocketHook socketHook) {
        socketHook.authServer.setSessionKey(key);
    }
}
