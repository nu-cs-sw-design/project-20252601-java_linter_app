package domain;

import domain.internal_representation.ClassInfo;
import domain.Violation;
import domain.internal_representation.Context;

import java.util.List;

/**
 * Interface for all lint checks. Each check analyzes a list of classes and returns violations found.
 */
public interface LintCheck {


    List<Violation> analyze(Context context);

    String getName();

    String getDescription();
}