package me.dags.daflight.launch;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightTransformer implements IClassTransformer
{
    private Logger logger = LogManager.getLogger("DaFlightTransformer");

    private Obf clazz = Obf.PlayerControllerMP;
    private Obf method = Obf.getPlayerMethod;
    private Desc methodDesc = Obf.getPlayerDesc;
    private Desc desc = Obf.initPlayerDesc;

    private boolean transformed = false;

    @Override
    public final byte[] transform(String className, String transformedName, byte[] classBytes)
    {
        if (!transformed)
        {
            if (transformedName.equals(clazz.get(false)))
            {
                transformed = true;
                return transform(classBytes, false);
            }
            else if (transformedName.equals(clazz.get(true)))
            {
                transformed = true;
                return transform(classBytes, true);
            }
        }
        return classBytes;
    }

    public byte[] transform(byte[] classBytes, boolean isObf)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(classBytes);
        reader.accept(node, 0);
        for (MethodNode mn : node.methods)
        {
            if (mn.name.equals(method.get(isObf)) && mn.desc.equals(methodDesc.get(isObf)))
            {
                logger.info("Found method: " + method.get(false) + "...");
                InsnList list = new InsnList();
                for (AbstractInsnNode n = mn.instructions.getFirst(); n != null; n = n.getNext())
                {
                    if (n.getOpcode() == Opcodes.NEW)
                    {
                        logger.info("Found Point 1");
                        list.add(new TypeInsnNode(Opcodes.NEW, "me/dags/daflight/EntityFlying"));
                    }
                    else if (n.getOpcode() == Opcodes.INVOKESPECIAL)
                    {
                        logger.info("Found Point 2");
                        list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "me/dags/daflight/EntityFlying", "<init>", desc.get(isObf), false));
                    }
                    else
                    {
                        list.add(n);
                    }
                }
                if (list.size() == mn.instructions.size())
                {
                    logger.info("Transforming: " + clazz.get(false) + "...");
                    mn.instructions.clear();
                    mn.instructions.add(list);
                    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                    node.accept(writer);
                    return writer.toByteArray();
                }
                else
                {
                    logger.warn("Unable to transform class: " + clazz.get(false) + "!");
                }
            }
        }
        return classBytes;
    }
}
