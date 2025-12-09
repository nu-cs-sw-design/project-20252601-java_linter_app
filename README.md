# Project: Java Linter

## Contributors
Anant Poddar, Aanand Patel

## Dependencies
- Language and version
    - Java with JDK 11
    - ASM (ObjectWeb ASM library for bytecode analysis)
    - Gradle (for building and running the project)
    - Python3 and the associated pip package manager
    - GenUML (for generating PlantUML diagrams; ensure it's installed using `pip install genuml`)

## Project Structure
The project follows a standard Gradle structure with a focus on clean architecture (presentation, domain, datasource layers). Here's the high-level structure:

- **Key Directories**:
    - `src/main/java/datasource/`: Handles loading `.class` files and converting bytecode to internal models using ASM.
    - `src/main/java/domain/`: Contains the lint engine, checks, and internal representations (e.g., ClassInfo for class structures).
    - `src/main/java/presentation/`: Console-based UI for running the linter and displaying results.
    - `src/main/java/test/`: Contains sample Java source files to be compiled and linted (see "Where Test Files Are Presented" below).
    - `src/main/java/compiled_test/`: Output directory for compiled `.class` files from the `test/` sources; this is where you point the linter.

## How to Run the Whole Project
1. **Build the Project**:
    - Run `./gradlew build` (or `gradlew.bat build` on Windows) to compile the linter (in root directory) .

2. **Run the Linter**:
    - Run `./gradlew run` (or `gradlew.bat run` on Windows) (in root directory).
    - When prompted, enter the path to a folder containing compiled `.class` files (e.g., `src/main/java/compiled_test/test/` for test classes).
    - Select lint checks by number (e.g., "1,3,5" or "all").
    - The linter will analyze the classes and display violations.
    - If GenerateUML is selected, a `design.puml` file will be created in the input folder.

You can provide absolute paths (e.g., `/Users/username/project/compiled_test`) as well.

**IMP**: If you get an error saying 'Unsupported class file major version 65', make sure that you compile the .java files using the command provided in "Compiling Test Classes" section.

## Where Test Files Are Presented
Test files (sample Java classes for linting) are located in `src/main/java/test/`. These are source `.java` files designed to demonstrate various lint issues, such as naming violations, circular dependencies, or redundant interfaces.

To use the compiled test classes:
- Pass `src/main/java/compiled_test/test/` as the input folder when prompted during runtime.

These .java test files are presented as examples to test the linter's functionality. You can add your own `.java` files for custom testing.


## Compiling Test Classes
To lint classes, you need compiled `.class` files. Use this command to compile Java sources with Java 11 compatibility:

`javac -source 11 -target 11 -d compiled_test -g test/*.java`

In this command, `-d compiled_test` specifies the output directory for compiled `.class` files.
and `test/*.java` specifies the source files to compile.

## Linter Checks
The linter includes several checks implemented as classes in `src/main/java/domain/`.

1. **Equals/HashCode Check**

2. **Public Mutable Fields Check**

3. **Naming Convention Check**

4. **Redundant Interfaces Check**

5. **Circular Dependency Check**

6. **Generate PlantUML** 

7. **Public Constructor Check**