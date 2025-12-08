package domain;

import domain.internal_representation.FieldInfo;
import java.util.Optional;

/**
 * Checks for public fields that are not final (mutable).
 */
public class PublicMutableFieldsCheck extends PerClassLintCheck {

    @Override
    protected Optional<Violation> checkField(FieldInfo field) {
        // Check if field is public and not final (mutable)
        if (field.isPublic() && !field.isFinal()) {
            String message = "Field '" + field.getName() + "' is public and mutable";
            return Optional.of(new Violation(getName(), field.getClassName(), message));
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return "Public Mutable Fields Check";
    }

    @Override
    public String getDescription() {
        return "Detects public fields that are not final (mutable)";
    }
}
