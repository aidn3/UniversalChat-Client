
package com.aidn5.universalchat.server;

import com.aidn5.universalchat.server.auth.ChatAuth;
import com.aidn5.universalchat.server.packets.*;
import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SocketHook {
    private static final String SERVER_URL = "wss://chat.aidn5.com";
    private static final Gson GSON = new Gson();
    private static final Set<Class<? extends IPacket>> packets = new HashSet<>();

    static {
        packets.add(AvailableReceptor.class);
        packets.add(Broadcast.class);
        packets.add(Message.class);
        packets.add(Role.class);
        packets.add(Session.class);
    }

    public final ChatAuth authServer = new ChatAuth();
    public final ServiceProvider serviceProvider = new ServiceProvider(this);
    private final Socket socket;

    public SocketHook() throws URISyntaxException {
        IO.Options options = IO.Options.builder()
                .setAuth(authServer.authData)
                .setSecure(true)
                .build();

        socket = IO.socket(SERVER_URL, options);


        for (Class<? extends IPacket> c : packets) {
            socket.on(c.getSimpleName(),
                    (Object[] payload) -> receivePacket(c, String.valueOf(payload[0])));
        }

        socket.on(Socket.EVENT_CONNECT_ERROR, this::onConnectionError);
        socket.on(Socket.EVENT_CONNECT, this::onConnect);

        updateAuth();
        socket.connect();
    }

    public void sendMessage(Message packet) {
        serviceProvider.sendChat(packet);
    }

    void sendPacket(IPacket packet) {
        String json = GSON.toJson(packet);
        String name = packet.getClass().getSimpleName();

        socket.emit(name, json);
    }

    private void receivePacket(Class<? extends IPacket> c, String json) {
        IPacket packet = GSON.fromJson(json, c);
        packet.processPacket(this);
    }

    private void onConnect(Object[] payload) {
        System.out.println("Socket Connected.");
    }

    private void onConnectionError(Object[] payload) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));

            updateAuth();
            socket.connect();

        } catch (InterruptedException e) {
            System.out.println("socket cool down is interrupted. not connecting anymore.");
        }
    }

    private void updateAuth() {
        try {
            authServer.updateAuth();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not auth with Mojang Session Servers. Connecting anyways...");
        }
    }
}
