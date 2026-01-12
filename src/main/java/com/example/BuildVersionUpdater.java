package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class responsible for updating build/version information
 * in project configuration files based on environment variables.
 *
 **/
public class BuildVersionUpdater {

    /** Environment variable name for the build number */
    private static final String ENV_BUILD_NUM = "BuildNum";

    /** Environment variable name for the source base path */
    private static final String ENV_SOURCE_PATH = "SourcePath";

    /** Relative path from SourcePath to the directory containing the target files */
    private static final String RELATIVE_DIR = "develop/global/src";

    /** Regex pattern to match the build version in SConstruct file.*/
    private static final Pattern SCONSTRUCT_PATTERN = Pattern.compile("point=\\d+");

    /** Regex pattern to match the SDK version point in VERSION file.*/
    private static final Pattern VERSION_PATTERN = Pattern.compile("ADLMSDK_VERSION_POINT=\\d+");

    public static void main(String[] args) throws IOException {

        // Read the build number from environment variables
        String buildNum = getRequiredEnv(ENV_BUILD_NUM);

        // Construct the absolute path to the source directory
        Path sourceDir = Paths.get(getRequiredEnv(ENV_SOURCE_PATH), RELATIVE_DIR);

        // Update the SConstruct file with the new build number
        updateFile(sourceDir.resolve("SConstruct"), SCONSTRUCT_PATTERN, "point=" + buildNum);

        // Update the VERSION file with the new SDK version point
        updateFile(sourceDir.resolve("VERSION"), VERSION_PATTERN, "ADLMSDK_VERSION_POINT=" + buildNum);

        // Log successful update
        System.out.println("Build version updated successfully to " + buildNum);
    }

    private static void updateFile(Path file, Pattern pattern, String replacement) throws IOException {

        // Ensure the target file exists before attempting modification
        if (!Files.exists(file)) {
            throw new IOException("File not found: " + file);
        }

        // Read all lines, apply regex replacement, and collect updated content
        List<String> updatedLines = Files.readAllLines(file, StandardCharsets.UTF_8)
                .stream()
                .map(line -> pattern.matcher(line).replaceAll(replacement))
                .collect(Collectors.toList());

        // Write the updated content back to the same file
        Files.write(file, updatedLines, StandardCharsets.UTF_8);
    }

    /**
     * Retrieves a required environment variable.
     *
     * <p>
     * If the variable is missing or empty, an exception is thrown
     * to prevent the program from running in an invalid state.
     * </p>
     *
     * @param key the environment variable name
     * @return the value of the environment variable
     * @throws IllegalStateException if the variable is not defined or empty
     */
    private static String getRequiredEnv(String key) {
        String value = System.getenv(key);
        // Validate that the environment variable is present and non-empty
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Required environment variable missing: " + key);
        }
        return value;
    }
}
