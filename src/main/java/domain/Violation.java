package domain;

public class Violation {
    final String checkName;
    final String className;
    final String message;

    public Violation(String checkName, String className, String message) {
        this.checkName = checkName;
        this.className = className;
        this.message = message;
    }

    public String getCheckName() {
        return checkName;
    }

    public String getClassName() {
        return className;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return checkName + " violated in " + className + ": " + message;
    }
}
