package domain.internal_representation;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents the dependency relationships between classes in the analyzed codebase.
 * Uses an adjacency matrix to store the relationship type between each pair of classes.
 *
 * Relationship types:
 * - NONE: No relationship (or self-reference)
 * - IS_A: Class P extends Class Q (P is a subclass of Q)
 * - HAS_A: Class P has a field of type Class Q
 * - IMPLEMENTS: Class P implements interface Q
 * - GENERAL: At least one of the following:
 *   - A method in Class P returns type Class Q
 *   - A method in Class P has a parameter of type Class Q
 *   - A method in Class P has a local variable of type Class Q
 */
public class DependencyInfo {
    private final DependencyType[][] adjacencyMatrix;
    private final Map<String, Integer> classNameToIndex;
    private final int size;

    public DependencyInfo(Map<String, Integer> classNameToIndex, int size) {
        this.classNameToIndex = classNameToIndex;
        this.size = size;
        this.adjacencyMatrix = new DependencyType[size][size];
        initializeMatrix();
    }

    private void initializeMatrix() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                adjacencyMatrix[i][j] = DependencyType.NONE;
            }
        }
    }

    public DependencyType getDependency(String fromClass, String toClass) {
        Integer fromIndex = classNameToIndex.get(fromClass);
        Integer toIndex = classNameToIndex.get(toClass);

        if (fromIndex == null || toIndex == null) {
            return DependencyType.NONE;
        }

        return adjacencyMatrix[fromIndex][toIndex];
    }


    public void setDependency(String fromClass, String toClass, DependencyType type) {
        Integer fromIndex = classNameToIndex.get(fromClass);
        Integer toIndex = classNameToIndex.get(toClass);

        if (fromIndex != null && toIndex != null) {
            adjacencyMatrix[fromIndex][toIndex] = type;
        }
    }


    public int getClassIndex(String className) {
        return classNameToIndex.getOrDefault(className, -1);
    }

    public int getSize() {
        return size;
    }
}