package domain;

import domain.internal_representation.ClassInfo;
import domain.internal_representation.Context;
import domain.internal_representation.MethodInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if public classes have public constructors (either explicit or implicit).
 * A public class with no explicit constructor has an implicit public constructor.
 * A public class with an explicit public constructor violates this check.
 */
public class HasPublicConstructorCheck implements LintCheck {

    @Override
    public List<Violation> analyze(Context context) {
        List<Violation> violations = new ArrayList<>();

        for (ClassInfo classInfo : context.getClasses()) {
            if (classInfo.isPublic()) {
                checkPublicConstructor(classInfo, violations);
            }
        }

        return violations;
    }

    private void checkPublicConstructor(ClassInfo classInfo, List<Violation> violations) {
        String className = classInfo.getName();
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);

        List<MethodInfo> constructors = getConstructors(classInfo, simpleClassName);

        if (constructors.isEmpty()) {
            // No explicit constructor means implicit public constructor
            violations.add(new Violation(
                    getName(),
                    className,
                    "Public class has no explicit constructor, resulting in an implicit public constructor"
            ));
        } else {
            // Check for explicit public constructors
            for (MethodInfo constructor : constructors) {
                if (constructor.isPublic()) {
                    violations.add(new Violation(
                            getName(),
                            className,
                            "Public class has an explicit public constructor: " + constructor.getName()
                    ));
                }
            }
        }
    }

    private List<MethodInfo> getConstructors(ClassInfo classInfo, String simpleClassName) {
        List<MethodInfo> constructors = new ArrayList<>();

        for (MethodInfo method : classInfo.getMethods()) {
            // Constructors are named "<init>" in bytecode
            if (method.getName().equals("<init>")) {
                constructors.add(method);
            }
        }

        return constructors;
    }

    @Override
    public String getName() {
        return "Public Constructor Check";
    }

    @Override
    public String getDescription() {
        return "Checks if public classes have public constructors (explicit or implicit). " +
                "Public classes should not expose public constructors to prevent direct instantiation.";
    }
}