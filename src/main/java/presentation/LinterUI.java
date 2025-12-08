package presentation;

import domain.Violation;
import java.util.List;

/**
 * Interface for the linter user interface.
 */
public interface LinterUI {

    /**
     * Runs the linter application.
     * Handles user interaction, file loading, analysis, and result display.
     */
    void run();

    /**
     * Displays the violations found during linting.
     */
    void displayResults(List<Violation> violations);
}