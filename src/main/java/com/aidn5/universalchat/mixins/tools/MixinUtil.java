package com.aidn5.universalchat.mixins.tools;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.util.Iterator;

public class MixinUtil {
    public static void injectMethodCall(MethodNode methodNode, Method method, int loadArgs) {
        Iterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

        while (iterator.hasNext()) {
            AbstractInsnNode node = iterator.next();

            if (node.getOpcode() == Opcodes.ALOAD) {
                for (int i = 0; i < loadArgs; i++) {
                    methodNode.instructions.insertBefore(node, new VarInsnNode(Opcodes.ALOAD, i + 1));
                }

                methodNode.instructions.insertBefore(node, createMethodCall(method));

                LabelNode exitNode = new LabelNode();
                methodNode.instructions.insertBefore(node, new JumpInsnNode(Opcodes.IFEQ, exitNode));
                methodNode.instructions.insertBefore(node, new InsnNode(Opcodes.RETURN));
                methodNode.instructions.insertBefore(node, exitNode);

                break;
            }
        }
    }

    private static MethodInsnNode createMethodCall(Method method) {
        String owner = Type.getInternalName(method.getDeclaringClass());

        return new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                owner,
                method.getName(),
                Type.getMethodDescriptor(method),
                false
        );
    }
}
