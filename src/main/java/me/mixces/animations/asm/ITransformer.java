package me.mixces.animations.asm;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;

public interface ITransformer
{

    String getClassName();

    void transform(ClassNode classNode, String name);

    default InsnList createInsnList(AbstractInsnNode... instructions)
    {
        final InsnList instructionList = new InsnList();

        for (AbstractInsnNode instruction : instructions)
        {
            instructionList.add(instruction);
        }

        return (instructionList);
    }

}
