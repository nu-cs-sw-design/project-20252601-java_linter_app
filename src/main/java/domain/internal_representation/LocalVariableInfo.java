package domain.internal_representation;

/**
 * Represents the internal representation of a local variable within a method.
 * Contains the variable's name and type information.
 */
public class LocalVariableInfo {
    private final String name;
    private final String type;

    public LocalVariableInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

}