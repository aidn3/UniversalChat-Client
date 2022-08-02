
package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.MessageDisplay;
import com.aidn5.universalchat.bridge.GuildBridge;
import com.aidn5.universalchat.server.SocketHook;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/*
 * MessagePacket is created separately to allow complicated API clients to take
 * advantage from the extra metadata added along with the packet.
 * Since this client is basic,
 * this class only utilizes username and message fields.
 */
public final class Message implements IPacket {
    /**
     * Username of the message's owner.
     *
     * <p>
     * Username field is ignored from client->server
     * unless the client has a special permission authorizing it
     * to pretend to be other people.
     * This feature is used for other cluster-servers/bots
     * that connect multiple people with a single endpoint.
     */
    @Nonnull
    public final String username;

    /**
     * If a player has a nickname or a guild-bridge
     * is sending messages on behalf of other players.
     */
    @Nullable
    public final String displayName;

    /**
     * The message to display.
     * <p>
     * The message is filtered by default to only include UTF-8 characters.
     * Any other character will be removed. If the final message is empty,
     * it will be completely ignored.
     */
    @Nonnull
    public final String message;

    /**
     * Set by the server. Indicates if the message is just a duplicate
     * of the sender received back from a chat message.
     * <p>
     * Used to avoid bots from resending a message they already sent
     * </p>
     */
    public final boolean self;

    public Message(@Nonnull String username, @Nullable String displayName,
                   @Nonnull String message, boolean self) {

        this.username = Objects.requireNonNull(username);
        this.displayName = displayName;
        this.message = Objects.requireNonNull(message);
        this.self = self;
    }

    @Override
    public void processPacket(@Nonnull SocketHook hook) {
        MessageDisplay.showGlobalMessage(this);
        GuildBridge.passServerMessage(this);
    }
}
