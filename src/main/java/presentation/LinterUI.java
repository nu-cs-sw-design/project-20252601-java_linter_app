package presentation;

import domain.LintEngine;
import domain.Violation;
import java.util.List;

/**
 * Interface for the linter user interface.
 */
public interface LinterUI {

    /**
     * Displays the violations found during linting.
     */
    void displayResults(List<Violation> violations);

    /**
     * Prompts the user for the folder path.
     */
    String getFolderPath();

    /**
     * Configures the lint checks, asking and prompting the user and adding to the engine.
     */
    void configureChecks(LintEngine engine);
}