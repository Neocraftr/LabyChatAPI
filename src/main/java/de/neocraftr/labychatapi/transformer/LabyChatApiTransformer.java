package de.neocraftr.labychatapi.transformer;

import net.labymod.core.asm.global.ClassEditor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class LabyChatApiTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(name.equals("net.labymod.labyconnect.ClientConnection")) {
            ClassEditor editor = new ClientConnectionEditor();
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(basicClass);
            reader.accept(node, 0);
            editor.accept(name, node);
            ClassWriter writer = new ClassWriter(3);
            node.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }
}
