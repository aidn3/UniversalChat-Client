
package com.aidn5.universalchat.server.auth;

import com.aidn5.universalchat.UniversalChat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ChatAuth {

    public final Map<String, String> authData = new HashMap<>();

    public void updateAuth() throws IOException {
        String key = authData.get("key");

        authData.clear();

        authData.put("key", key);

        authData.put("username", MojangAuth.playerUsername());
        authData.put("uuid", MojangAuth.playerUuid());
        authData.put("spectator", String.valueOf(!MojangAuth.isValidSession()));

        if (MojangAuth.isValidSession()) {
            String secret = MojangAuth.sendJoinRequest();
            authData.put("secret", secret);

        } else {
            System.out.println("Session is in offline mode.");
        }

        authData.put("appName", UniversalChat.MODID);
        authData.put("appVersion", UniversalChat.VERSION);
    }

    public void setSessionKey(String key) {
        authData.put("key", key);
    }
}
