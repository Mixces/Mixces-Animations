package me.mixces.animations.asm;

import me.mixces.animations.asm.transformer.ItemRendererTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Taken from Scherso :)
 */
public class ClassTransformer implements IClassTransformer
{

    final public static boolean DUMP_BYTECODE = Boolean.parseBoolean(System.getProperty("MixcesAnimations.debugBytecode", "false"));
    final static int EOL = -1;
    final private HashMap<String, List<ITransformer>> TRANSFORMER_HASHMAP = new HashMap<>();

    public ClassTransformer()
    {
        registerTransformer(new ItemRendererTransformer());
    }

    private void registerTransformer(ITransformer transformer)
    {
        final List<ITransformer> transformer_list = TRANSFORMER_HASHMAP.get(transformer.getClassName());
        if (transformer_list == null)
        {
            final List<ITransformer> new_list = new ArrayList<>();
            new_list.add(transformer);
            TRANSFORMER_HASHMAP.put(transformer.getClassName(), new_list);
        }
        else
        {
            transformer_list.add(transformer);
        }
    }

    @Override
    public byte[] transform(final String name, final String transformedName, byte[] bytes)
    {
        if (bytes == null)
        {
            return null;
        }

        final List<ITransformer> transformer_list = TRANSFORMER_HASHMAP.get(transformedName);

        if (transformer_list == null)
        {
            return bytes;
        }

        for (int i = transformer_list.size() - 1; i > EOL; i--)
        {
            final ITransformer transformer = transformer_list.get(i);
            final ClassNode cn = new ClassNode();
            final ClassReader cr = new ClassReader(bytes);
            cr.accept(cn, 0);
            transformer.transform(cn, transformedName);
            final ClassWriter cw = new ClassWriter(cr, 0);
            cn.accept(cw);

            if (DUMP_BYTECODE)
            {
                this.dumpBytes(transformedName, cw);
            }

            bytes = cw.toByteArray();
        }
        return (bytes);
    }

    private void dumpBytes(String name, ClassWriter writer)
    {
        try
        {
            name = (name.contains("$")) ? name.replace('$', '.') + ".class" : name + ".class";

            final File bytecode_dir = new File(".bytecode.out");

            if (!bytecode_dir.exists())
            {
                bytecode_dir.mkdirs();
            }

            final File bytecode_out = new File(bytecode_dir, name);

            if (!bytecode_out.exists())
            {
                bytecode_out.createNewFile();
            }

            FileOutputStream stream = new FileOutputStream(bytecode_out);
            stream.write(writer.toByteArray());
            stream.close();
        }
        catch (Exception ex)
        {
            System.out.println("Failed to dump bytecode for " + name);
            ex.printStackTrace();
        }
    }

}