package domain;

import domain.internal_representation.ClassInfo;
import domain.internal_representation.MethodInfo;

import java.util.List;
import java.util.Optional;

/**
 * Checks if public classes expose public constructors (explicit or implicit).
 */
public class HasPublicConstructorCheck extends PerClassLintCheck {

    @Override
    protected Optional<Violation> checkClass(ClassInfo classInfo) {
        // Only care about public and concrete classes
        if (!classInfo.isPublic() || !classInfo.isConcrete()) {
            return Optional.empty();
        }

        String className = classInfo.getName();
        List<MethodInfo> methods = classInfo.getMethods();

        boolean hasAnyConstructor = false;
        boolean hasPublicConstructor = false;

        for (MethodInfo method : methods) {
            // look at methods named "<init>"
            if ("<init>".equals(method.getName())) {
                hasAnyConstructor = true;
                if (method.isPublic()) {
                    hasPublicConstructor = true;
                    break;
                }
            }
        }

        // No explicit constructor
        if (!hasAnyConstructor) {
            String message = "Public class has no explicit constructor, resulting in an implicit public constructor.";
            return Optional.of(new Violation(getName(), className, message));
        }

        if (hasPublicConstructor) {
            String message = "Public class has an explicit public constructor,allowing direct instantiation.";
            return Optional.of(new Violation(getName(), className, message));
        }

        return Optional.empty();
    }

    @Override
    public String getName() {
        return "Public Constructor Check";
    }

    @Override
    public String getDescription() {
        return "Flags public classes that expose public constructors (explicit or implicit), to discourage direct instantiation.";
    }
}