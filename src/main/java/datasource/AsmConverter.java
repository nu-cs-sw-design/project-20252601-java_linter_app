package datasource;

import domain.internal_representation.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter class that adapts the ASM library to the DataModelConverter interface.
 * Converts ASM's ClassNode structure into our domain ClassInfo structure.
 */
public class AsmConverter implements DataModelConverter {

    private final AsmLibrary adaptee;

    public AsmConverter() {
        this.adaptee = new AsmLibrary();
    }

    @Override
    public ClassInfo convertClass(byte[] bytes) {
        ClassNode classNode = adaptee.readClassNode(bytes);

        // Extract basic class information
        String name = Type.getObjectType(classNode.name).getClassName();
        String superClass = classNode.superName != null ?
                Type.getObjectType(classNode.superName).getClassName() : null;
        boolean isPublic = (classNode.access & Opcodes.ACC_PUBLIC) != 0;

        // Convert interfaces
        List<String> interfaces = ((List<String>) classNode.interfaces).stream()
                .map(iface -> Type.getObjectType(iface).getClassName())
                .collect(Collectors.toList());

        // Convert fields
        List<FieldInfo> fields = adaptee.getFields(classNode).stream()
                .map(fieldNode -> convertField(fieldNode, name))
                .collect(Collectors.toList());

        // Convert methods
        List<MethodInfo> methods = adaptee.getMethods(classNode).stream()
                .map(methodNode -> convertMethod(methodNode, name))
                .collect(Collectors.toList());

        return new ClassInfo(name, fields, methods, interfaces, superClass, isPublic);
    }

    /**
     * Converts an ASM FieldNode to our domain FieldInfo.
     */
    private FieldInfo convertField(FieldNode fieldNode, String className) {
        String fieldName = fieldNode.name;
        String type = Type.getType(fieldNode.desc).getClassName();
        boolean isPublic = (fieldNode.access & Opcodes.ACC_PUBLIC) != 0;
        boolean isFinal = (fieldNode.access & Opcodes.ACC_FINAL) != 0;

        return new FieldInfo(fieldName, className, type, isPublic, isFinal);
    }

    /**
     * Converts an ASM MethodNode to our domain MethodInfo.
     */
    private MethodInfo convertMethod(MethodNode methodNode, String className) {
        String methodName = methodNode.name;
        String returnType = Type.getReturnType(methodNode.desc).getClassName();
        boolean isPublic = (methodNode.access & Opcodes.ACC_PUBLIC) != 0;
        boolean isStatic = (methodNode.access & Opcodes.ACC_STATIC) != 0;

        List<LocalVariableInfo> localVariables = new ArrayList<>();
        if (methodNode.localVariables != null) {
            for (LocalVariableNode localVar : methodNode.localVariables) {
                // Skip 'this' parameter for non-static methods
                if (!localVar.name.equals("this")) {
                    String varType = Type.getType(localVar.desc).getClassName();
                    localVariables.add(new LocalVariableInfo(localVar.name, varType));
                }
            }
        }

        return new MethodInfo(methodName, className, returnType, isPublic, isStatic, localVariables);
    }
}