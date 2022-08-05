package com.aidn5.universalchat.mixins;

import com.aidn5.universalchat.mixins.tools.MixinUtil;
import com.aidn5.universalchat.mixins.tools.TransformerName;
import com.aidn5.universalchat.mixins.tools.TransformerTarget;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

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
        Method method = Arrays.stream(PlayerCallback.class.getMethods())
                .filter(m -> m.getName().equals("onChatMessage"))
                .findFirst().get();

        int loadArgs = 1;
        MixinUtil.injectMethodCall(mn, method, loadArgs);
    }


}
