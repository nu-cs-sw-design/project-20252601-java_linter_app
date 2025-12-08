package domain;

import domain.internal_representation.ClassInfo;
import domain.internal_representation.MethodInfo;

import java.util.Optional;

public class EqualsHashCodeCheck extends PerClassLintCheck {

    @Override
    protected Optional<Violation> checkClass(ClassInfo classInfo) {

        boolean hasEquals = false;
        boolean hasHashcode = false;
        for (MethodInfo method : classInfo.getMethods()) {
            if (method.getName().equals("equals")) {
                hasEquals = true;
            }
            if (method.getName().equals("hashCode")) {
                hasHashcode = true;
            }
        }
        if (hasEquals != hasHashcode) {
            String message = hasEquals ?
                    "Class overrides equals() but not hashCode()" :
                    "Class overrides hashCode() but not equals()";
            return Optional.of(new Violation(getName(), classInfo.getName(), message));
        }
        return Optional.empty();
    }
    @Override
    public String getName() {
        return "Equals/HashCode Check";
    }

    @Override
    public String getDescription() {
        return "Detects classes that override equals() or hashCode() but not both";
    }
}
