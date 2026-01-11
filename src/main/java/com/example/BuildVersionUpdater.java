package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BuildVersionUpdater {

    private static final String ENV_BUILD_NUM = "BuildNum";
    private static final String ENV_SOURCE_PATH = "SourcePath";
    private static final String RELATIVE_DIR = "develop/global/src";
    private static final Pattern SCONSTRUCT_PATTERN = Pattern.compile("point=\\d+");
    private static final Pattern VERSION_PATTERN = Pattern.compile("ADLMSDK_VERSION_POINT=\\d+");

    public static void main(String[] args) throws IOException {
        String buildNum = getRequiredEnv(ENV_BUILD_NUM);
        Path sourceDir = Paths.get(getRequiredEnv(ENV_SOURCE_PATH), RELATIVE_DIR);

        updateFile(sourceDir.resolve("SConstruct"), SCONSTRUCT_PATTERN, "point=" + buildNum);

        updateFile(sourceDir.resolve("VERSION"), VERSION_PATTERN, "ADLMSDK_VERSION_POINT=" + buildNum);

        System.out.println("Build version updated successfully to " + buildNum);
    }

    private static void updateFile(Path file, Pattern pattern, String replacement) throws IOException {

        if (!Files.exists(file)) {
            throw new IOException("File not found: " + file);
        }

        List<String> updatedLines = Files.readAllLines(file, StandardCharsets.UTF_8)
                .stream()
                .map(line -> pattern.matcher(line).replaceAll(replacement))
                .collect(Collectors.toList());

        Files.write(file, updatedLines, StandardCharsets.UTF_8);
    }

    private static String getRequiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Required environment variable missing: " + key);
        }
        return value;
    }
}
