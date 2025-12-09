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
    public String getFolderPath() {
        System.out.println("===========================================");
        System.out.println("  Java Linter");
        System.out.println("===========================================");

        System.out.println("Please enter the path to compiled classes folder:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }
    public String getChecksSelectionInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available Lint Checks:");
        System.out.println("1. Equals/HashCode Check - Detects classes that override one but not both");
        System.out.println("2. Public Mutable Fields Check - Detects public non-final fields");
        System.out.println("3. Naming Convention Check - Check if names obey conventions");
        System.out.println("4. Redundant Interfaces Check - Detects interfaces already implemented by superclass or ancestor classses");
        System.out.println("5. Circular Dependency Check - Detects circular dependencies between classes, either directly or indirectly");
        System.out.println("6. Generate PlantUML - Generate PlantUML code for the given compiled classes");
        System.out.println("7. Public Constructor Check: Checks if public classes expose public constructors (explicit or implicit)");
        System.out.println("Select checks to run (comma-separated, e.g., 1,2,4) or 'all' for all checks:");

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("all")) {
            return input;
        }

        // allow only digits, commas, spaces
        if (!input.matches("[0-9, ]+")) {
            System.err.println("Invalid input format. Expected digits separated by commas.");
            System.exit(1);
        }

        // validate each number is between 1–7:
        String[] parts = input.split(",");
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part.trim());
                if (num < 1 || num > 7) {
                    System.err.println("Invalid check number: " + num);
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number: " + part);
                System.exit(1);
            }
        }

        return input;
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

}