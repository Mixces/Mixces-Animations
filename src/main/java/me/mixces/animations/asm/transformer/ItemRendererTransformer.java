package me.mixces.animations.asm.transformer;

import me.mixces.animations.asm.ITransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class ItemRendererTransformer implements ITransformer {

    @Override
    public String getClassName() {
        return "net.minecraft.client.renderer.ItemRenderer";
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (final MethodNode method : classNode.methods)
        {
            final String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, method.name, method.desc);
            int f1 = -1;

            if (methodName.equals("renderItemInFirstPerson") || methodName.equals("func_78440_a"))
            {
                for (final LocalVariableNode variableNode : method.localVariables)
                {
                    if (variableNode.name.equals("f1"))
                    {
                        f1 = variableNode.index;
                    }
                }

                for (final AbstractInsnNode INSN : method.instructions.toArray())
                {
                    if (INSN.getOpcode() == INVOKESPECIAL)
                    {
                        final MethodInsnNode methodInsnNode = (MethodInsnNode) INSN;
                        final AbstractInsnNode previousNode = methodInsnNode.getPrevious();
                        final String methodNode = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodInsnNode.name, methodInsnNode.desc);

                        if (methodNode.equals("transformFirstPersonItem") || methodNode.equals("func_178096_b"))
                        {
                            if (previousNode.getOpcode() == FCONST_0) {
                                method.instructions.remove(previousNode);
                                method.instructions.insertBefore(methodInsnNode, createInsnList(
                                        new InsnNode(previousNode.getOpcode()),
                                        new VarInsnNode(FLOAD, f1),
                                        new MethodInsnNode(INVOKESTATIC, "me/mixces/animations/TransformerHook", "localVar", "(FF)F", false)
                                ));
                            }
                        }
                    }
                }
            }
        }
    }

}
