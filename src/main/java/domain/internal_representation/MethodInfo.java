package domain.internal_representation;

import java.util.List;

/**
 * Represents the internal representation of a Java method.
 * Contains all relevant information about the method structure.
 */
public class MethodInfo {
    private final String name;
    private final String className;
    private final String returnType;
    private final boolean isPublic;
    private final boolean isStatic;
    private final List<LocalVariableInfo> localVariables;

    public MethodInfo(String name, String className, String returnType, boolean isPublic, boolean isStatic, List<LocalVariableInfo> localVariables) {
        this.name = name;
        this.className = className;
        this.returnType = returnType;
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.localVariables = localVariables;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getReturnType() {
        return returnType;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public List<LocalVariableInfo> getLocalVariables() {
        return localVariables;
    }

}