package domain.internal_representation;

import java.util.List;
import java.util.Map;
/**
 * Represents the complete context of the analyzed codebase.
 * Contains all ClassInfo objects and their dependency relationships.
 */
public class Context {
    private final List<ClassInfo> classes;
    private final DependencyInfo dependencyInfo;
    private final String folderPath;
    private final Map<String, byte[]> classBytecode;

    public Context(List<ClassInfo> classes, DependencyInfo dependencyInfo,  String folderPath,Map<String, byte[]> classBytecode) {
        this.classes = classes;
        this.dependencyInfo = dependencyInfo;
        this.folderPath = folderPath;
        this.classBytecode = classBytecode;
    }

    public List<ClassInfo> getClasses() {
        return classes;
    }

    public DependencyInfo getDependencyInfo() {
        return dependencyInfo;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public byte[] getClassBytecode(String className) {
        return classBytecode.get(className);

    }

    public Map<String,byte[]>  getBytecodeMap() {
        return classBytecode;

    }

    public int getClassCount() {
        return classes.size();
    }

}