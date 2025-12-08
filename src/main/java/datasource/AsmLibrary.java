package datasource;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * Adaptee class that wraps the actual ASM library functionality.
 * This class directly interacts with ASM's functions to read bytes
 */
public class AsmLibrary {

    /**
     * Reads class bytes and returns an ASM ClassNode representing the class.
     */
    public ClassNode readClassNode(byte[] bytes) {

        ClassReader reader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        return classNode;
    }

    /**
     * Extracts the list of fields from a ClassNode.
     */
    @SuppressWarnings("unchecked")
    public List<FieldNode> getFields(ClassNode classNode) {
        return (List<FieldNode>) classNode.fields;
    }

    /**
     * Extracts the list of methods from a ClassNode.
     */
    @SuppressWarnings("unchecked")
    public List<MethodNode> getMethods(ClassNode classNode) {
        return (List<MethodNode>) classNode.methods;
    }
}
