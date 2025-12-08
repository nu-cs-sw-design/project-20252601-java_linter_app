package domain;

import domain.internal_representation.Context;
import domain.internal_representation.ClassInfo;

import java.util.*;

/**
 * Checks for redundant interface declarations.
 * A class redundantly declares an interface if its superclass already implements it.
 */

public class RedundantInterfacesCheck implements LintCheck {

    @Override
    public List<Violation> analyze(Context context) {
            List<Violation> violations = new ArrayList<>();

            // Build a map of all classes for lookup
            Map<String, ClassInfo> classMap = new HashMap<>();
            for (ClassInfo classInfo : context.getClasses()) {
                classMap.put(classInfo.getName(), classInfo);
            }

            // Check each class for redundant interfaces
            for (ClassInfo classInfo : context.getClasses()) {
                Set<String> parentInterfaces = collectParentInterfaces(classInfo, classMap);

                // Check if any of the class's declared interfaces are already in parent
                for (String declaredInterface : classInfo.getInterfaces()) {
                    if (parentInterfaces.contains(declaredInterface)) {
                        String message = "Interface '" + declaredInterface + "' is redundant (already implemented by superclass)";
                        violations.add(new Violation(getName(), classInfo.getName(), message));
                    }
                }
            }

            return violations;
    }

    private Set<String> collectParentInterfaces(ClassInfo classInfo, Map<String, ClassInfo> classMap) {
        Set<String> interfaces = new HashSet<>();
        String superClassName = classInfo.getSuperClass();

        while (superClassName != null && !superClassName.equals("java.lang.Object")) {
            ClassInfo superClass = classMap.get(superClassName);
            if (superClass == null) {
                break;
            }

            interfaces.addAll(superClass.getInterfaces());
            superClassName = superClass.getSuperClass();
        }
        return interfaces;
    }

    @Override
    public String getName() {
        return "Redundant Interfaces Check";
    }

    @Override
    public String getDescription() {
        return "Detects interfaces that are redundantly declared because they are already implemented parent or any ancestor class";
    }
}
