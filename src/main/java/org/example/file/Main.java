package org.example.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Invalid usage. Please provide the output file path as an argument.");
            System.err.println("Example: java -jar your-jar-file.jar output/output.txt");
            System.exit(1);
        }

        String outputFilePath = args[0];
        try {
            Paths.get(outputFilePath);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path: " + outputFilePath);
            e.printStackTrace();
            System.exit(1);
        }

        File outputFile = new File(outputFilePath);

        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
                System.exit(1);
            }
        }
        try (FileWriter writer = new FileWriter(outputFile)) {
            processFiles(writer);
        } catch (IOException e) {
            System.err.println("Error while writing to the file: " + e.getMessage());
            logger.warning("Error while writing to the file");
        }

        System.out.println("Output file created at: " + outputFile.getAbsolutePath());
    }

    private static void processFiles(FileWriter writer) {
        try {
            String[] filePaths = {
                    "src/main/resources/Folder2/File2-1.txt",
                    "src/main/resources/Folder1/File1-1.txt"
            };

            for (String filePath : filePaths) {
                Path path = Paths.get(filePath);
                if (Files.exists(path)) {
                    logger.info("Appending file: " + path.toAbsolutePath());

                    Files.lines(path).forEach(line -> {
                        try {
                            writer.write(line + System.lineSeparator());
                        } catch (IOException e) {
                            logger.severe("Error writing line to file: " + e.getMessage());
                        }
                    });
                } else {
                    logger.severe("File does not exist: " + path.toAbsolutePath());
                }
            }
        } catch (IOException e) {
            logger.severe("Error processing files: " + e.getMessage());
        }
    }
}
