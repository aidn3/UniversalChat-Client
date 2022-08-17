package com.aidn5.universalchat.config;

import com.aidn5.universalchat.common.Channel;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("CanBeFinal")
public class ConfigInstance {
    public transient File configPath;

    public boolean showGlobalChat = true;
    @Nonnull
    public Channel currentChannel = Channel.NONE;

    public boolean showBroadcastUpdates = true;

    public boolean bridgeEnabled = false;

    public void save() throws IOException {
        ConfigUtil.saveConfig(configPath, this);
    }
}
