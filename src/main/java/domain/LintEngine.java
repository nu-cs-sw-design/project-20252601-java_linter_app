package domain;

import domain.internal_representation.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * The main engine that coordinates running lint checks on classes.
 * Maintains a collection of checks and applies them to analyzed classes.
 */
public class LintEngine {
    private final List<LintCheck> checks = new ArrayList<>();

    // new constructor
    public LintEngine(String input) {

        if (input.equalsIgnoreCase("all")) {
            checks.add(new EqualsHashCodeCheck());
            checks.add(new PublicMutableFieldsCheck());
            checks.add(new NamingConventionCheck());
            checks.add(new RedundantInterfacesCheck());
            checks.add(new CircularDependencyCheck());
            checks.add(new GenerateUML());
            checks.add(new HasPublicConstructorCheck());
            return;
        }

        String[] selections = input.split(",");
        for (String selection : selections) {
            switch (selection.trim()) {
                case "1":
                    checks.add(new EqualsHashCodeCheck());
                    break;
                case "2":
                    checks.add(new PublicMutableFieldsCheck());
                    break;
                case "3":
                    checks.add(new NamingConventionCheck());
                    break;
                case "4":
                    checks.add(new RedundantInterfacesCheck());
                    break;
                case "5":
                    checks.add(new CircularDependencyCheck());
                    break;
                case "6":
                    checks.add(new GenerateUML());
                    break;
                case "7":
                    checks.add(new HasPublicConstructorCheck());
                    break;

            }
        }

    }

    public int getCheckCount() {
        return checks.size();
    }

    public List<Violation> analyzeAll(Context context) {
        List<Violation> allViolations = new ArrayList<>();

        for (LintCheck check : checks) {
            System.out.println("  Running: " + check.getName());
            allViolations.addAll(check.analyze(context));
        }

        return allViolations;
    }
}