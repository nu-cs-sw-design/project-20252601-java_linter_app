package domain;

import domain.internal_representation.*;

import java.util.Optional;

/**
 * Checks naming conventions for classes, fields, methods, and variables.
 *
 * Rules for nmaes:
 * - Classes/Interfaces: Start with uppercase, no special characters or numbers
 * - Methods/Variables: Start with lowercase, no special characters or numbers
 * - Constants (final fields):All uppercase, no special characters or numbers
 * - Special characters checked: _ and $
 */
public class NamingConventionCheck extends PerClassLintCheck {

    @Override
    protected Optional<Violation> checkClass(ClassInfo classInfo) {
        String name = classInfo.getName();

        if (!beginsWithUppercase(name) || hasSpecialCharacter(name) || hasNumber(name)) {
            String message = "Class name '" + name + "' does not follow naming conventions " +
                    "(should start with uppercase, no special characters or numbers)";
            return Optional.of(new Violation(getName(), classInfo.getName(), message));
        }
        return Optional.empty();
    }

    @Override
    protected Optional<Violation> checkField(FieldInfo field) {
        String name = field.getName();

        // Constants should be all uppercase
        if (field.isFinal()) {
            if (!hasAllUppercase(name) || hasSpecialCharacter(name) || hasNumber(name)) {
                String message = "Constant '" + name + "' does not follow naming conventions " +
                        "(should be all uppercase, no special characters or numbers)";
                return Optional.of(new Violation(getName(), field.getClassName(), message));
            }
        }
        // Regular fields should start with lowercase
        else {
            if (!beginsWithLowercase(name) || hasSpecialCharacter(name) || hasNumber(name)) {
                String message = "Field '" + name + "' does not follow naming conventions " +
                        "(should start with lowercase, no special characters or numbers)";
                return Optional.of(new Violation(getName(), field.getClassName(), message));
            }
        }
        return Optional.empty();
    }

    @Override
    protected Optional<Violation> checkMethod(MethodInfo method) {
        String name = method.getName();

        // Skip constructors and special methods
        if (name.equals("<init>") ||name.equals("<clinit>")) {
            return Optional.empty();
        }

        if (!beginsWithLowercase(name) || hasSpecialCharacter(name) || hasNumber(name)) {
            String message = "Method '" + name + "' does not follow naming conventions " +
                    "(should start with lowercase, no special characters or numbers)";
            return Optional.of(new Violation(getName(), method.getClassName(), message));
        }
        return Optional.empty();
    }

    @Override
    protected Optional<Violation> checkLocalVariable(LocalVariableInfo variable, MethodInfo method) {
        String name = variable.getName();

        // Skip 'this' parameter
        if (name.equals("this")) {
            return Optional.empty();
        }

        if (!beginsWithLowercase(name) || hasSpecialCharacter(name) || hasNumber(name)) {
            String message = "Local variable '" + name + "' in method '" + method.getName() +
                    "' does not follow naming conventions " +
                    "(should start with lowercase, no special characters or numbers)";
            return Optional.of(new Violation(getName(), method.getClassName(), message));
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "Naming Convention Check";
    }

    @Override
    public String getDescription() {
        return "Validates that class, method, field, and variable names follow naming conventions";
    }

    // Helper methods

    private boolean beginsWithUppercase(String name) {
        return name.length() > 0 && Character.isUpperCase(name.charAt(0));
    }

    private boolean beginsWithLowercase(String name) {
        return name.length() > 0 && Character.isLowerCase(name.charAt(0));
    }

    private boolean hasAllUppercase(String name) {
        for (char c : name.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNumber(String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSpecialCharacter(String name) {
        for (char c : name.toCharArray()) {
            if (c == '_' || c == '$') {
                return true;
            }
        }
        return false;
    }

}
