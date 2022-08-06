
package com.aidn5.universalchat;

import com.aidn5.universalchat.bridge.GuildBridge;
import com.aidn5.universalchat.commands.ChatCommand;
import com.aidn5.universalchat.commands.MessageCommand;
import com.aidn5.universalchat.commands.UniversalCommand;
import com.aidn5.universalchat.config.ConfigInstance;
import com.aidn5.universalchat.config.ConfigUtil;
import com.aidn5.universalchat.server.SocketHook;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URISyntaxException;

@Mod(
        modid = UniversalChat.MODID,
        version = UniversalChat.VERSION,
        acceptedMinecraftVersions = "1.8.9",
        clientSideOnly = true
)
public class UniversalChat {
    public static final String MODID = "UniversalChat";
    public static final String VERSION = "1.0";

    public static ConfigInstance configInstance;
    public static SocketHook socketHook;

    @EventHandler
    public void preInit(@Nonnull FMLPreInitializationEvent event) throws IOException {
        configInstance = ConfigUtil.loadConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(@Nonnull FMLInitializationEvent event) throws URISyntaxException {
        socketHook = new SocketHook();

        MinecraftForge.EVENT_BUS.register(new GuildBridge());

        ClientCommandHandler.instance.registerCommand(new MessageCommand());
        ClientCommandHandler.instance.registerCommand(new ChatCommand());
        ClientCommandHandler.instance.registerCommand(new UniversalCommand());
    }
}
