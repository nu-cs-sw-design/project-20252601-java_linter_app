package domain.internal_representation;

import java.util.List;

/**
 * Represents the internal representation of a Java class.
 * Contains all info about the class structure.
 */
public class ClassInfo {
    private final String name;
    private final List<FieldInfo> fields;
    private final List<MethodInfo> methods;
    private final List<String> interfaces;
    private final String superClass;
    private final boolean isPublic;

    public ClassInfo(String name, List<FieldInfo> fields, List<MethodInfo> methods, List<String> interfaces, String superClass, boolean isPublic) {
        this.name = name;
        this.fields = fields;
        this.methods = methods;
        this.interfaces = interfaces;
        this.superClass = superClass;
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public String getSuperClass() {
        return superClass;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean hasMethod(String name) {
        return methods.stream().anyMatch(method -> method.getName().equals(name));
    }

}
