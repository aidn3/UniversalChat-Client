package com.aidn5.universalchat.mixins.tweaker;

import com.aidn5.universalchat.mixins.MinecraftMessageMixin;
import com.aidn5.universalchat.mixins.tools.TransformerTarget;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Transformer implements IClassTransformer {
    final Set<TransformerTarget> transformers = new HashSet<>();

    public Transformer() {
        transformers.add(new MinecraftMessageMixin());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        Set<TransformerTarget> found = transformers.stream()
                .filter((t) -> t.targetClass.match(name) || t.targetClass.match(transformedName))
                .collect(Collectors.toSet());

        if (found.isEmpty()) return basicClass;
        System.out.println("Visiting class " + name + ", " + transformedName);

        ClassReader classReader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        classReader.accept(node, 0);

        for (TransformerTarget target : found) {

            Optional<MethodNode> method = node.methods.stream()
                    .filter(m -> target.targetMethod.match(m.name))
                    .filter(m -> target.targetMethodDesc.match(m.desc))
                    .findFirst();
            if (method.isPresent()) {
                target.transform(method.get());

            } else {
                System.err.println("Class " + target.targetClass.getClean()
                        + " Method " + target.targetMethod.getClean()
                        + " is not found");
            }
        }

        ClassWriter classWriter = new ClassWriter(0);
        node.accept(classWriter);
        return classWriter.toByteArray();
    }
}
