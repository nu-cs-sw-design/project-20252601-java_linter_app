package presentation;

import domain.LintEngine;
import domain.Violation;
import java.util.List;

/**
 * Interface for the linter user interface.
 */
interface LinterUI {

    /**
     * Displays the violations found during linting.
     */
    void displayResults(List<Violation> violations);

    /**
     * Prompts the user for the folder path.
     */
    String getFolderPath();

    /**
     * Prompting the user to select the lint checks to run.
     */
    String getChecksSelectionInput();
}