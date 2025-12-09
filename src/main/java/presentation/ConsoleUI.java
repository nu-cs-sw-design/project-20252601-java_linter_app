package presentation;

import domain.LintEngine;
import domain.*;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based implementation of the LinterUI.
 */
class ConsoleUI implements LinterUI {


    /**
     * Prompts the user for the folder path.
     */
     String getFolderPath() {
        System.out.println("===========================================");
        System.out.println("  Java Linter");
        System.out.println("===========================================");

        System.out.println("Please enter the path to compiled classes folder:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }

    /**
     * Displays the violations found.
     */
    @Override
    public void displayResults(List<Violation> violations) {
        System.out.println("===========================================");
        System.out.println("  Lint Results");
        System.out.println("===========================================");

        if (violations.isEmpty()) {
            System.out.println("No violations found are found within the code");
        } else {
            System.out.println("Found " + violations.size() + " violation(s):\n");

            int count = 1;
            for (Violation violation : violations) {
                System.out.println(count + ". " + violation.toString());
                System.out.println();
                count++;
            }
        }

        System.out.println("===========================================");
    }

    /**
     * Prompts the user to select which lint checks to run and adds them to the lint engine
     */
    public void configureChecks(LintEngine engine) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available Lint Checks:");
        System.out.println("1. Equals/HashCode Check - Detects classes that override one but not both");
        System.out.println("2. Public Mutable Fields Check - Detects public non-final fields");
        System.out.println("3. Naming Convention Check - Check if names obey conventions");
        System.out.println("4. Redundant Interfaces Check - Detects interfaces already implemented by superclass or ancestor classses");
        System.out.println("5. Circular Dependency Check - Detects circular dependencies between classes, either directly or indirectly");
        System.out.println("6. Generate PlantUML - Generate PlantUML code for the given compiled classes");
        System.out.println("Select checks to run (comma-separated, e.g., 1,2,4) or 'all' for all checks:");

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("all")) {
            addAllChecks(engine);
        } else {
            String[] selections = input.split(",");
            for (String selection : selections) {
                try {
                    int checkNumber = Integer.parseInt(selection.trim());
                    addCheck(engine, checkNumber);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input: " + selection);
                }
            }
        }

        System.out.println();
        System.out.println("Configured " + engine.getCheckCount() + " check(s)");
        System.out.println();
    }

    /**
     * Adds all available checks to the engine.
     */
    private void addAllChecks(LintEngine engine) {
        engine.addCheck(new EqualsHashCodeCheck());
        engine.addCheck(new PublicMutableFieldsCheck());
        engine.addCheck(new NamingConventionCheck());
        engine.addCheck(new RedundantInterfacesCheck());
        engine.addCheck(new CircularDependencyCheck());
        engine.addCheck(new GenerateUML());
    }

    /**
     * Adds a specific check to the engine based on user selection.
     */
    private void addCheck(LintEngine engine, int checkNumber) {
        switch (checkNumber) {
            case 1:
                engine.addCheck(new EqualsHashCodeCheck());
                break;
            case 2:
                engine.addCheck(new PublicMutableFieldsCheck());
                break;
            case 3:
                engine.addCheck(new NamingConventionCheck());
                break;
            case 4:
                engine.addCheck(new RedundantInterfacesCheck());
                break;
            case 5:
                engine.addCheck(new CircularDependencyCheck());
                break;
            case 6:
                engine.addCheck(new GenerateUML());
                break;
            default:
                System.err.println("  ✗ Invalid check number: " + checkNumber);
        }
    }
}