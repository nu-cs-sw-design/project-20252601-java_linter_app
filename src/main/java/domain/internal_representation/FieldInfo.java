package domain.internal_representation;

/**
 * Represents the internal representation of a Java field.
 * Contains all relevant information about the field.
 */
public class FieldInfo {
    private final String name;
    private final String className;
    private final String type;
    private final boolean isPublic;
    private final boolean isFinal;

    public FieldInfo(String name, String className, String type,
                     boolean isPublic, boolean isFinal) {
        this.name = name;
        this.className = className;
        this.type = type;
        this.isPublic = isPublic;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getType() {
        return type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isFinal() {
        return isFinal;
    }

}