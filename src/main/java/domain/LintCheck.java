package domain;

import domain.internal_representation.ClassInfo;
import domain.Violation;

import java.util.List;

/**
 * Interface for all lint checks. Each check analyzes a list of classes and returns violations found.
 */
public interface LintCheck {


    List<Violation> analyze(List<ClassInfo> classes);

    String getName();

    String getDescription();
}