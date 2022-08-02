package com.aidn5.universalchat.mixins;

import com.aidn5.universalchat.UniversalChat;
import com.aidn5.universalchat.common.Channel;
import com.aidn5.universalchat.common.MessageUtil;
import com.aidn5.universalchat.mixins.tools.MixinUtil;
import com.aidn5.universalchat.mixins.tools.TransformerName;
import com.aidn5.universalchat.mixins.tools.TransformerTarget;
import com.aidn5.universalchat.server.packets.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MinecraftMessageMixin extends TransformerTarget {
    public MinecraftMessageMixin() {
        this.targetClass = new TransformerName(
                "net.minecraft.client.entity.EntityPlayerSP",
                "net.minecraft.client.entity.EntityPlayerSP",
                "bew"
        );

        this.targetMethod = new TransformerName(
                "sendChatMessage",
                "func_71165_d",
                "e"
        );

        this.targetMethodDesc = new TransformerName(
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class)),
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class)),
                Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class))
        );
    }

    @Override
    public void transform(MethodNode mn) {
        Method method = Arrays.stream(MinecraftMessageMixin.class.getMethods())
                .filter(m -> m.getName().equals("onChatMessage"))
                .findFirst().get();

        int loadArgs = 1;
        MixinUtil.injectMethodCall(mn, method, loadArgs);
    }

    // return true to exit method, called by asm from #transform
    @SuppressWarnings("unused")
    public static boolean onChatMessage(String message) {
        if (message.startsWith("/")) return false;
        if (UniversalChat.configInstance.currentChannel == Channel.NONE) return false;

        if (!UniversalChat.configInstance.showGlobalChat) {
            UniversalChat.configInstance.currentChannel = Channel.NONE;
            Minecraft.getMinecraft().thePlayer.addChatMessage(MessageUtil.getBackDefaultMessage());

            try {
                UniversalChat.configInstance.save();
            } catch (IOException e) {
                e.printStackTrace();
                Minecraft.getMinecraft().thePlayer.addChatMessage(MessageUtil.getErrorSavingSettings());
            }

            return false;
        }

        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        String displayName = null;
        Message messagePacket = new Message(p.getName(), displayName, message, true);
        UniversalChat.socketHook.sendMessage(messagePacket);
        return true;
    }
}
