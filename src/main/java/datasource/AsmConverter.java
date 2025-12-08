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
import java.util.Map;
import java.util.HashMap;

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
    public Context buildContext(Map<String, byte[]> classFiles, String folderPath) {

        List<ClassInfo> classes = new ArrayList<>();
        Map<String, byte[]> classBytecodeMap = new HashMap<>();

        for (Map.Entry<String, byte[]> entry : classFiles.entrySet()) {
            ClassInfo classInfo = convertClass(entry.getValue());
            classes.add(classInfo);
            classBytecodeMap.put(classInfo.getName(), entry.getValue());
        }

        DependencyInfo dependencyInfo = convertDependencies(classes);

        return new Context(classes, dependencyInfo, folderPath, classBytecodeMap);
    }

    /**
     * Analyzes all classes and builds the dependency information.
     */
    public DependencyInfo convertDependencies(List<ClassInfo> classes) {
        // Build index map for quick lookups
        Map<String, Integer> classNameToIndex = new HashMap<>();
        for (int i = 0; i < classes.size(); i++) {
            classNameToIndex.put(classes.get(i).getName(), i);
        }

        DependencyInfo dependencyInfo = new DependencyInfo(classNameToIndex, classes.size());

        for (ClassInfo classInfo : classes) {
            String className = classInfo.getName();

            // Analyze IS_A relationships (superclass)
            if (classInfo.getSuperClass() != null && !classInfo.getSuperClass().isEmpty()) {
                dependencyInfo.setDependency(className, classInfo.getSuperClass(), DependencyType.IS_A);
            }

            // Analyze IMPLEMENTS relationships
            for (String interfaceName : classInfo.getInterfaces()) {
                dependencyInfo.setDependency(className, interfaceName, DependencyType.IMPLEMENTS);
            }

            // Analyze HAS_A relationships (fields)
            for (FieldInfo field : classInfo.getFields()) {
                String fieldType = field.getType();
                if (dependencyInfo.getClassIndex(fieldType) != -1) {
                    dependencyInfo.setDependency(className, fieldType, DependencyType.HAS_A);
                }
            }

            // Analyze GENERAL relationships (methods)
            for (MethodInfo method : classInfo.getMethods()) {
                // Check return type
                String returnType = method.getReturnType();
                if (returnType != null && !returnType.equals("void") &&
                        dependencyInfo.getClassIndex(returnType) != -1) {
                    DependencyType currentType = dependencyInfo.getDependency(className, returnType);
                    if (currentType == DependencyType.NONE) {
                        dependencyInfo.setDependency(className, returnType, DependencyType.GENERAL);
                    }
                }

                // Check local variables (includes parameters)
                for (LocalVariableInfo localVar : method.getLocalVariables()) {
                    String varType = localVar.getType();
                    if (dependencyInfo.getClassIndex(varType) != -1) {
                        DependencyType currentType = dependencyInfo.getDependency(className, varType);
                        if (currentType == DependencyType.NONE) {
                            dependencyInfo.setDependency(className, varType, DependencyType.GENERAL);
                        }
                    }
                }
            }
        }

        return dependencyInfo;
    }


    private String getSimpleName(String fullyQualifiedName) {
        int lastDot = fullyQualifiedName.lastIndexOf('.');
        return lastDot >= 0 ? fullyQualifiedName.substring(lastDot + 1) : fullyQualifiedName;
    }

    public ClassInfo convertClass(byte[] bytes) {
        ClassNode classNode = adaptee.readClassNode(bytes);

        // Extract basic class information
        String name = getSimpleName(Type.getObjectType(classNode.name).getClassName());
        String superClass = classNode.superName != null ? getSimpleName(Type.getObjectType(classNode.superName).getClassName()) : null;
        boolean isPublic = (classNode.access & Opcodes.ACC_PUBLIC) != 0;

        // Convert interfaces
        List<String> interfaces = ((List<String>) classNode.interfaces).stream().map(iface -> getSimpleName(Type.getObjectType(iface).getClassName())).collect(Collectors.toList());

        // Convert fields
        List<FieldInfo> fields = adaptee.getFields(classNode).stream().map(fieldNode -> convertField(fieldNode, name)).collect(Collectors.toList());

        // Convert methods
        List<MethodInfo> methods = adaptee.getMethods(classNode).stream().map(methodNode -> convertMethod(methodNode, name)).collect(Collectors.toList());

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