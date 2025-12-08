package presentation;

import datasource.DataLoader;
import datasource.AsmConverter;
import datasource.DataModelConverter;
import domain.*;
import domain.internal_representation.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Console-based implementation of the LinterUI.
 */
public class ConsoleUI implements LinterUI {

    private final LintEngine engine;
    private final DataModelConverter converter;
    private final DataLoader loader;
    private final String folderPath;

    public ConsoleUI(String folderPath) {
        this.folderPath = folderPath;
        this.engine = new LintEngine();
        this.converter = new AsmConverter();
        this.loader = new DataLoader();
    }

    @Override
    public void run() {
        System.out.println("===========================================");
        System.out.println("  Java Linter");
        System.out.println("===========================================");
        System.out.println("Analyzing folder: " + folderPath);
        System.out.println();

        // Step 1: Get user input for which checks to run
        configureChecks();

        // Step 2: Load class files from folder
        Map<String, byte[]> classFiles;
        try {
            System.out.println("Loading class files...");
            classFiles = loader.loadClassFiles(folderPath);
        } catch (IOException e) {
            System.err.println("Error loading class files: " + e.getMessage());
            return;
        }

        // Step 3: Create the context
        System.out.println("Converting bytecode to internal representation...");
        Context context;
        try {
            context = converter.buildContext(classFiles, folderPath);
            System.out.println("Processed " + context.getClassCount() + " class(es)");
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error building context: " + e.getMessage());
            e.printStackTrace();
            return;
        };

        // Step 4: Run lint checks
        System.out.println("Running lint checks...");
        List<Violation> violations = engine.analyzeAll(context);
        System.out.println();

        // Step 5: Display results
        displayResults(violations);
    }

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
     * Prompts the user to select which lint checks to run.
     */
    private void configureChecks() {
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
            addAllChecks();
        } else {
            String[] selections = input.split(",");
            for (String selection : selections) {
                try {
                    int checkNumber = Integer.parseInt(selection.trim());
                    addCheck(checkNumber);
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
    private void addAllChecks() {
        engine.addCheck(new EqualsHashCodeCheck());
        engine.addCheck(new PublicMutableFieldsCheck());
        engine.addCheck(new NamingConventionCheck());
        engine.addCheck(new RedundantInterfacesCheck());
        engine.addCheck(new CircularDependencyCheck());
        engine.addCheck(new GenerateUML());
    }

    /**
     * Adds a specific check based on user selection.
     */
    private void addCheck(int checkNumber) {
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