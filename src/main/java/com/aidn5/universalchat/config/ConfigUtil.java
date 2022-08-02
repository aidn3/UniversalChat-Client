package com.aidn5.universalchat.config;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("UnstableApiUsage")
public class ConfigUtil {
    public final static Gson ConfigGsonBuilder = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static ConfigInstance loadConfig(File file) throws IOException {
        ConfigInstance config;

        if (Files.isFile().apply(file)) {
            String json = Files.toString(file, StandardCharsets.UTF_8);
            config = ConfigGsonBuilder.fromJson(json, ConfigInstance.class);

        } else {
            config = new ConfigInstance();
            saveConfig(file, config);
        }

        config.configPath = file;
        return config;
    }

    public static void saveConfig(File file, ConfigInstance config) throws IOException {
        String json = ConfigGsonBuilder.toJson(config);
        Files.write(json.getBytes(StandardCharsets.UTF_8), file);
    }
}
