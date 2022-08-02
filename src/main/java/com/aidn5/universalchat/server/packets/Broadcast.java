
package com.aidn5.universalchat.server.packets;

import com.aidn5.universalchat.MessageDisplay;
import com.aidn5.universalchat.server.SocketHook;
import net.minecraft.util.IChatComponent;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Generic message from the server-side.
 * It can be command response initiated by the client,
 * advertisements, mute notification, etc.
 *
 * @author aidn5
 */
/*
 * Developer note: single packet "broadcast" is created to have full control
 * from server side to display and edit anything. This way, anything needs to be
 * coded once on server-side, and it will immediately take effect.
 * <p>
 * It is possible to create a separate packet for every single event (mute
 * event, advertisement event, etc.) but that will be hard-coded and hard to
 * update later.
 * <p>
 * User chat is separated in MessagePacket.class
 */
public class Broadcast implements IPacket {
    /**
     * The broadcast/message content to display.
     * This can either be plain message or {@link IChatComponent} in json form,
     * depending on {@link #json} value.
     *
     * <p>
     * The message can contain any type of character and not limited by UTF-8.
     * Special characters such as <code>�</code> can be included.
     */
    @Nonnull
    public final String message;

    /**
     * Indicates whether {@link #message} is plain text
     * or {@link IChatComponent} serialized in json form.
     */
    public final boolean json;// TODO: fix

    /**
     * Whether the message is of an importance to be displayed and can't be
     * ignored/toggled.
     *
     * <p>
     * Important Messages like: Mute & Ban Notification, Commands response the
     * client sent, etc.
     * <br>
     * Not-important Messages like: advertising, suggestions messages
     */
    public final boolean important;


    public Broadcast(@Nonnull String message, boolean json, boolean important) {
        if (message.isEmpty()) {
            throw new IllegalArgumentException("message is empty");
        }

        this.message = Objects.requireNonNull(message);
        this.json = json;
        this.important = important;
    }

    @Override
    public void processPacket(@Nonnull SocketHook hook) {
        MessageDisplay.showBroadcastMessage(this);
    }
}
