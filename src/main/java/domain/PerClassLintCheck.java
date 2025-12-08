package domain;

import domain.internal_representation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Abstract base class implementing the Template Method pattern for checking each individual.
 * Provides a defined traversal algorithm over classes, fields, methods, and local variables.
 * Subclasses (specific checks) can override specific methods to implement their checking logic.
 */
public abstract class PerClassLintCheck implements LintCheck {

    /**
     * Template method that defines the analysis algorithm.
     * This method is final to enforce the same traversal pattern for all checks.
     */
    @Override
    public final List<Violation> analyze(Context context) {
        List<Violation> violations = new ArrayList<>();

        for (ClassInfo classInfo : context.getClasses()) {
            // Check at class level
            Optional<Violation> classViolation = checkClass(classInfo);
            classViolation.ifPresent(violations::add);

            // Check all fields
            for (FieldInfo field : classInfo.getFields()) {
                Optional<Violation> fieldViolation = checkField(field);
                fieldViolation.ifPresent(violations::add);
            }

            // Check all methods
            for (MethodInfo method : classInfo.getMethods()) {
                Optional<Violation> methodViolation = checkMethod(method);
                methodViolation.ifPresent(violations::add);

                // Check all local variables in the method
                for (LocalVariableInfo variable : method.getLocalVariables()) {
                    Optional<Violation> varViolation = checkLocalVariable(variable, method);
                    varViolation.ifPresent(violations::add);
                }
            }
        }

        return violations;
    }

    /**
     * Subclasses can override this to implement class-level checks.
     */
    protected Optional<Violation> checkClass(ClassInfo classInfo) {
        return Optional.empty();
    }

    /**
     * Subclasses can override this to implement field-level checks.
     */
    protected Optional<Violation> checkField(FieldInfo field) {
        return Optional.empty();
    }

    /**
     * Subclasses can override this to implement method-level checks.
     */
    protected Optional<Violation> checkMethod(MethodInfo method) {
        return Optional.empty();
    }

    /**
     * Subclasses can override this to implement variable-level checks.
     */
    protected Optional<Violation> checkLocalVariable(LocalVariableInfo variable, MethodInfo method) {
        return Optional.empty();
    }

    public abstract String getDescription();
}
