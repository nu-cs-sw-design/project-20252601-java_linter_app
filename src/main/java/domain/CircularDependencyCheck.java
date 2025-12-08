package domain;

import domain.internal_representation.ClassInfo;
import domain.internal_representation.Context;
import domain.internal_representation.DependencyInfo;
import domain.internal_representation.DependencyType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Detects circular dependencies between classes.
 * Uses depth-first search to find cycles in the dependency graph.
 */
public class CircularDependencyCheck implements LintCheck {

    @Override
    public List<Violation> analyze(Context context) {
        List<Violation> violations = new ArrayList<>();

        DependencyInfo dependencyInfo = context.getDependencyInfo();
        List<ClassInfo> classes = context.getClasses();

        Set<String> globalVisited = new HashSet<>();

        for (ClassInfo classInfo : classes) {
            String className = classInfo.getName();

            if (!globalVisited.contains(className)) {
                Set<String> visitedInPath = new HashSet<>();
                List<String> path = new ArrayList<>();

                detectCycle(className, dependencyInfo, classes, visitedInPath, path, globalVisited, violations);
            }
        }

        return violations;
    }

    private void detectCycle(String currentClass, DependencyInfo dependencyInfo, List<ClassInfo> classes, Set<String> visitedInPath, List<String> path, Set<String> globalVisited, List<Violation> violations)
    {

        if (visitedInPath.contains(currentClass)) {
            // Cycle detected
            int cycleStart = path.indexOf(currentClass);
            List<String> cycle = new ArrayList<>(path.subList(cycleStart, path.size()));
            cycle.add(currentClass);

            String cyclePath = String.join(" -> ", cycle);
            violations.add(new Violation(getName(),currentClass, "Circular dependency detected: " + cyclePath));
            return;
        }

        if (globalVisited.contains(currentClass)) {
            return;
        }

        visitedInPath.add(currentClass);
        path.add(currentClass);

        for (ClassInfo classInfo : classes) {
            String dependentClass = classInfo.getName();
            DependencyType depType = dependencyInfo.getDependency(currentClass, dependentClass);

            if (depType != DependencyType.NONE) {
                detectCycle(dependentClass, dependencyInfo, classes, visitedInPath, path, globalVisited, violations);
            }
        }

        path.remove(path.size() - 1);
        visitedInPath.remove(currentClass);
        globalVisited.add(currentClass);
    }

    @Override
    public String getName() {
        return "Circular Dependency Check";
    }

    @Override
    public String getDescription() {
        return "Detects circular dependencies between classes where Class A depends on Class B and Class B depends on Class A (directly or indirectly).";
    }
}