package domain;

import domain.internal_representation.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * The main engine that coordinates running lint checks on classes.
 * Maintains a collection of checks and applies them to analyzed classes.
 */
public class LintEngine {
    private final List<LintCheck> checks;

    public LintEngine() {
        this.checks = new ArrayList<>();
    }

    public void addCheck(LintCheck check) {
        checks.add(check);
    }

    public int getCheckCount() {
        return checks.size();
    }

    /**
     * Analyzes all given classes using all added checks.
     */
    public List<Violation> analyzeAll(Context context) {
        List<Violation> allViolations = new ArrayList<>();

        for (LintCheck check : checks) {
            System.out.println("  Running: " + check.getName());
            List<Violation> violations = check.analyze(context);
            allViolations.addAll(violations);
        }

        return allViolations;
    }
}
