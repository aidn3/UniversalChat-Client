package com.aidn5.universalchat.mixins.tools;

import org.objectweb.asm.tree.MethodNode;

public abstract class TransformerTarget {
    public TransformerName targetClass;

    public TransformerName targetMethod;
    public TransformerName targetMethodDesc;

    public abstract void transform(MethodNode mn);
}
