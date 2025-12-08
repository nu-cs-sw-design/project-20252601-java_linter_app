package domain;

import domain.internal_representation.ClassInfo;
import domain.internal_representation.Context;
import domain.internal_representation.DependencyInfo;
import domain.internal_representation.DependencyType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a PlantUML diagram for the entire package using GenUML.
 * Creates a .puml file in the specified folder path with all classes and their dependencies.
 */
public class GenerateUML implements LintCheck {

    @Override
    public List<Violation> analyze(Context context) {
        List<Violation> violations = new ArrayList<>();

        try {
            String pumlContent = generatePlantUmlDiagram(context);
            writePlantUmlFile(context.getFolderPath(), pumlContent);
        } catch (Exception e) {
            violations.add(new Violation(
                    getName(),
                    "PackageGeneration",
                    "Failed to generate PlantUML diagram: " + e.getMessage()
            ));
        }

        return violations;
    }

    private String generatePlantUmlDiagram(Context context) throws IOException, InterruptedException {
        StringBuilder plantUml = new StringBuilder();
        plantUml.append("@startuml\n\n");

        // Create temporary directory for class files
        Path tempDir = Files.createTempDirectory("genuml_temp");

        try {
            // Generate class definitions using GenUML
            for (ClassInfo classInfo : context.getClasses()) {
                String className = classInfo.getName();
                byte[] bytecode = context.getClassBytecode(className);

                if (bytecode != null) {
                    String classDefinition = generateClassDefinition(className, bytecode, tempDir);
                    plantUml.append(classDefinition).append("\n");
                }
            }

            // Add dependency relationships
            plantUml.append(generateDependencyRelationships(context));

        } finally {
            // Clean up temporary directory
            deleteDirectory(tempDir.toFile());
        }

        plantUml.append("\n@enduml\n");
        return plantUml.toString();
    }

    private String generateClassDefinition(String className, byte[] bytecode, Path tempDir)
            throws IOException, InterruptedException {

        // Write bytecode to temporary file
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        Path classFile = tempDir.resolve(simpleClassName + ".class");
        Files.write(classFile, bytecode);

        // Execute genuml command
        ProcessBuilder processBuilder = new ProcessBuilder(
                "genuml", "generate", classFile.toString()
        );
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip @startuml and @enduml lines as we'll add them ourselves
                if (!line.trim().equals("@startuml") && !line.trim().equals("@enduml")) {
                    output.append(line).append("\n");
                }
            }
        }

        process.waitFor();
        return output.toString();
    }

    private String generateDependencyRelationships(Context context) {
        StringBuilder relationships = new StringBuilder();
        relationships.append("' Relationships\n");

        DependencyInfo dependencyInfo = context.getDependencyInfo();
        List<ClassInfo> classes = context.getClasses();

        for (ClassInfo fromClass : classes) {
            String fromClassName = fromClass.getName();

            for (ClassInfo toClass : classes) {
                String toClassName = toClass.getName();
                DependencyType depType = dependencyInfo.getDependency(
                        fromClass.getName(), toClass.getName()
                );

                String arrow = getPlantUmlArrow(depType);
                if (arrow != null) {
                    relationships.append(fromClassName).append(" ").append(arrow).append(" ").append(toClassName).append("\n");
                }
            }
        }

        return relationships.toString();
    }

    private String getPlantUmlArrow(DependencyType type) {
        switch (type) {
            case IS_A:
                return "--|>";  // Inheritance (extends)
            case IMPLEMENTS:
                return "..|>";  // Implementation (implements)
            case HAS_A:
                return "*--";   // Composition
            case GENERAL:
                return "..>";   // Dependency
            case NONE:
            default:
                return null;
        }
    }

    private void writePlantUmlFile(String folderPath, String content) throws IOException {
        Path outputPath = Paths.get(folderPath, "design.puml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
            writer.write(content);
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    @Override
    public String getName() {
        return "Generate PlantUML Diagram";
    }

    @Override
    public String getDescription() {
        return "Generates a PlantUML class diagram for the entire package including all dependencies.";
    }
}