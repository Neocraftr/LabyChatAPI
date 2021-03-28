package de.neocraftr.labychatapi.transformer;

import de.neocraftr.labychatapi.LabyChatAPI;
import net.labymod.core.asm.global.ClassEditor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.Optional;

public class ClientConnectionEditor extends ClassEditor {

    public ClientConnectionEditor() {
        super(ClassEditorType.CLASS_NODE);
    }

    @Override
    public void accept(String name, ClassNode node) {
        getMethod(node, "handle", "(Lnet/labymod/labyconnect/packets/PacketMessage;)V").ifPresent(handleMethod -> {
            getVarInstruction(handleMethod, Opcodes.ALOAD, 0, Opcodes.GETFIELD).ifPresent(firstInstruction -> {
                InsnList beforeInstructions = new InsnList();
                beforeInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                beforeInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(LabyChatAPI.class),
                        "handleMessageEvent", "(Lnet/labymod/labyconnect/packets/PacketMessage;)V", false));
                handleMethod.instructions.insertBefore(firstInstruction, beforeInstructions);
            });
        });

        getMethod(node, "handle", "(Lnet/labymod/labyconnect/packets/PacketPlayFriendStatus;)V").ifPresent(handleMethod -> {
            getVarInstruction(handleMethod, Opcodes.ALOAD, 0, Opcodes.GETFIELD).ifPresent(firstInstruction -> {
                InsnList beforeInstructions = new InsnList();
                beforeInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                beforeInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(LabyChatAPI.class),
                        "handleServerChangeEvent", "(Lnet/labymod/labyconnect/packets/PacketPlayFriendStatus;)V", false));
                handleMethod.instructions.insertBefore(firstInstruction, beforeInstructions);
            });
        });

        getMethod(node, "handle", "(Lnet/labymod/labyconnect/packets/PacketPlayPlayerOnline;)V").ifPresent(handleMethod -> {
            getVarInstruction(handleMethod, Opcodes.ALOAD, 0, Opcodes.GETFIELD).ifPresent(firstInstruction -> {
                InsnList beforeInstructions = new InsnList();
                beforeInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                beforeInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(LabyChatAPI.class),
                        "handlePlayerOnlineEvent", "(Lnet/labymod/labyconnect/packets/PacketPlayPlayerOnline;)V", false));
                handleMethod.instructions.insertBefore(firstInstruction, beforeInstructions);
            });
        });
    }

    private Optional<MethodNode> getMethod(ClassNode node, String name, String descriptor) {
        return node.methods.stream().filter(methodNode -> methodNode.name.equals(name) && methodNode.desc.equals(descriptor)).findFirst();
    }

    private Optional<AbstractInsnNode> getVarInstruction(MethodNode method, int opcode, int value, int nextOpcode) {
        return Arrays.stream(method.instructions.toArray()).filter(abstractInsnNode -> isRightVarInstruction(abstractInsnNode, opcode, value, nextOpcode)).findFirst();
    }

    private boolean isRightVarInstruction(AbstractInsnNode instruction, int opcode, int value, int nextOpcode) {
        return instruction.getOpcode() == opcode && ((VarInsnNode) instruction).var == value && instruction.getNext().getOpcode() == nextOpcode;
    }

    private Optional<AbstractInsnNode> getInstruction(MethodNode method, int opcode, int beforeOpcode) {
        return Arrays.stream(method.instructions.toArray()).filter(abstractInsnNode -> isRightInstruction(abstractInsnNode, opcode, beforeOpcode)).findFirst();
    }

    private boolean isRightInstruction(AbstractInsnNode instruction, int opcode, int beforeOpcode) {
        return instruction.getOpcode() == opcode && instruction.getPrevious().getOpcode() == beforeOpcode;
    }
}
