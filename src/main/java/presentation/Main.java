package presentation;

import presentation.ConsoleUI;
import presentation.LinterUI;
/**
 * Main entry point for the Linter application.
 *
 * Usage: java linter.Main <folder_path>
 */
public class Main {

    public static void main(String[] args) {
        // make sure command args are provided
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String folderPath = args[0];

        // Create and run the console UI
        LinterUI ui = new ConsoleUI(folderPath);

        try {
            ui.run();
        } catch (Exception e) {
            System.err.println("An error occurred while running the linter:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Prints usage information for the application.
     */
    private static void printUsage() {
        System.err.println("Usage: java linter.LinterMain <folder_path>");
        System.err.println();
        System.err.println("Arguments:");
        System.err.println("  folder_path    Path to the folder containing .class files to analyze");
        System.err.println();
        System.err.println("Example:");
        System.err.println("  java linter.LinterMain ./target/classes");
    }
}
