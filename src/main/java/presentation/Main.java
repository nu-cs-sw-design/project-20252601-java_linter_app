package presentation;

import datasource.DataLoader;
import datasource.AsmConverter;
import datasource.DataModelConverter;
import domain.LintEngine;
import domain.Violation;
import domain.internal_representation.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main entry point for the Linter application. deals with the whole flow
 */
public class Main {

    public static void main(String[] args) {


        DataModelConverter converter = new AsmConverter();
        DataLoader loader = new DataLoader();
        ConsoleUI ui = new ConsoleUI();

        try {
            // Step 1: Get folder path from user
            String folderPath = ui.getFolderPath();
            if (folderPath.isEmpty()) {
                System.err.println("No folder path provided. Exiting.");
                System.exit(1);
            }
            System.out.println("Analyzing folder: " + folderPath);
            System.out.println();

            // Step 2: Load class files
            System.out.println("Loading class files...");
            Map<String, byte[]> classFiles = loader.loadClassFiles(folderPath);
            if (classFiles.isEmpty()) {
                System.err.println("Error: No .class files found in folder: " + folderPath);
                System.err.println("Make sure you provided the compiled classes directory, not the source (java) directory.");
                return;
            }

            // Step 3: Get checks from user and instantiate engine with those checks
            String input = ui.getChecksSelectionInput();
            LintEngine engine = new LintEngine(input);


            // Step 4: Build context
            System.out.println("Converting bytecode to internal representation...");
            Context context = converter.buildContext(classFiles, folderPath);
            System.out.println("Processed " + context.getClassCount() + " class(es)");
            System.out.println();

            // Step 5: Run lint checks
            System.out.println("Running lint checks...");
            List<Violation> violations = engine.analyzeAll(context);
            System.out.println();

            // Step 6: Display results via UI
            ui.displayResults(violations);
        } catch (IOException e) {
            System.err.println("Error during execution: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}