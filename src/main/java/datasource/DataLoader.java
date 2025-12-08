package datasource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Responsible for loading .class files from a folder path.
 * Returns a map of file paths to their byte array contents.
 */
public class DataLoader {

    public Map<String, byte[]> loadClassFiles(String folderPath) throws IOException {
        Path folder = Paths.get(folderPath);
        Map<String, byte[]> classFiles = new HashMap<>();

        if (!Files.exists(folder)) {
            throw new IOException("Folder does not exist: " + folderPath);
        }

        if (!Files.isDirectory(folder)) {
            throw new IOException("Path is not a directory: " + folderPath);
        }

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".class"))
                    .forEach(path -> {
                        try {
                            byte[] bytes = Files.readAllBytes(path);
                            classFiles.put(path.toString(), bytes);
                        } catch (IOException e) {
                            System.err.println("Error reading file: " + path);
                            e.printStackTrace();
                        }
                    });
        }

        return classFiles;
    }
}