
package com.aidn5.universalchat;

import com.aidn5.universalchat.bridge.GuildBridge;
import com.aidn5.universalchat.bridge.GuildChatCompactor;
import com.aidn5.universalchat.channel.ChatCommand;
import com.aidn5.universalchat.channel.ChatDetectChannel;
import com.aidn5.universalchat.channel.MessageCommand;
import com.aidn5.universalchat.channel.ReplyCommand;
import com.aidn5.universalchat.commands.SendCommand;
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
    public static final String VERSION = "1.1";

    public static ConfigInstance configInstance;
    public static SocketHook socketHook;

    @EventHandler
    public void preInit(@Nonnull FMLPreInitializationEvent event) throws IOException {
        configInstance = ConfigUtil.loadConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(@Nonnull FMLInitializationEvent event) throws URISyntaxException {
        socketHook = new SocketHook();

        MinecraftForge.EVENT_BUS.register(new GuildChatCompactor());
        MinecraftForge.EVENT_BUS.register(new GuildBridge());

        ClientCommandHandler.instance.registerCommand(new SendCommand());
        ClientCommandHandler.instance.registerCommand(new UniversalCommand());


        MinecraftForge.EVENT_BUS.register(new ChatDetectChannel());
        ClientCommandHandler.instance.registerCommand(new ReplyCommand());
        ClientCommandHandler.instance.registerCommand(new MessageCommand());
        ClientCommandHandler.instance.registerCommand(new ChatCommand());
    }
}
