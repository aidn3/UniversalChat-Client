
package com.aidn5.universalchat.server.auth;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

/**
 * Chat authorization is done based on Minecraft client-server login protocol.
 * It uses Mojang session servers to simulate login process to server to check
 * players' identity.
 * <b>This is used to prevent usernames spoofing and spam bots.</b>
 *
 * <pre>
 * Basic Idea:
 * - Use Mojang as a trusted third-party.
 * - Player Logs-in into his Mojang account and put a random ID and tell server to check.
 * - Server checks if the ID is indeed saved publicly on that Mojang account
 * - Since putting that ID requires the login data of that account, server can confirm the player is legit
 *
 * Client-side in-depth:
 * - Generate unique very long number and call it ID
 * - send the ID to mojang and tell mojang you are joining a server.
 *   -> authorize to mojang via your session id https://sessionserver.mojang.com/session/minecraft/join
 * - send the just generated ID to the server
 *
 * Server in-depth:
 * - contacts mojang https://sessionserver.mojang.com/session/minecraft/hasJoined and says:
 * - is a guy called username has authorized to you and told you he is joining a server with this ID?
 * - mojang answers yes-> that account is premium cause authorized with mojang
 * - mojang answers no -> fake
 *
 * NOTE: UniversalChat NEVER shares your session id with anyone
 * but sessionserver.mojang.com for basic authorization.
 * </pre>
 *
 * <b>Note: The protocol is slightly modified to avoid complicity.</b> *
 *
 * @author aidn5
 * @see <a href="https://wiki.vg/Protocol_Encryption#Authentication">wiki.vg/Protocol_Encryption</a>
 * @see <a href="https://wiki.vg/Protocol#Login">wiki.vg/Protocol#Login</a>
 * @see <a href="https://www.reddit.com/r/admincraft/comments/2ajrm2/how_joining_a_server_works">reddit.com/r/admincraft/comments/2ajrm2/how_joining_a_server_works</a>
 */
public class MojangAuth {
    private static final SecureRandom random = new SecureRandom();

    static boolean isValidSession() {
        Session session = Minecraft.getMinecraft().getSession();

        return session.getToken() != null
                && !session.getToken().isEmpty()
                && session.getToken().length() > 250;
    }

    static String playerUuid() {
        return Minecraft.getMinecraft().getSession().getPlayerID();
    }

    static String playerUsername() {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    static String sendJoinRequest() throws IOException {
        String uuid = playerUuid();
        String token = Minecraft.getMinecraft().getSession().getToken();

        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/join");

        String newSecret = generateUniqueId();
        JsonObject data = new JsonObject();
        data.addProperty("accessToken", token);
        data.addProperty("selectedProfile", uuid);
        data.addProperty("serverId", newSecret);

        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestProperty("Content-Type", "application/json");
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setDoInput(true);

        try (OutputStream os = c.getOutputStream()) {
            os.write(data.toString().getBytes());
        }

        int code = c.getResponseCode();
        if (code != 204) {
            throw new RuntimeException("Could not auth with mojang servers. server returned " + code);

        }

        return newSecret;
    }

    private static String generateUniqueId() {
        StringBuilder result = new StringBuilder();

        while (result.length() < 42) {
            byte b = (byte) random.nextInt();
            result.append(String.format("%02x", b));
        }

        return result.substring(0, 40);
    }
}
