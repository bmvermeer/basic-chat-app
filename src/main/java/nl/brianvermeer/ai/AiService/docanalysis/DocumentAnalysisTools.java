package nl.brianvermeer.ai.AiService.docanalysis;

import dev.langchain4j.agent.tool.Tool;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class DocumentAnalysisTools {

    @Inject
    SanitizationService sanitizationService;

    @Tool("Get all file content from a directory")
    public String analyzeDirectory(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                return "Error: The specified path does not exist or is not a directory: " + directoryPath;
            }

            List<Path> textFiles = findTextFiles(path);
            if (textFiles.isEmpty()) {
                return "No text files found in directory: " + directoryPath;
            }

            StringBuilder analysis = new StringBuilder();

            for (Path file : textFiles) {
                analysis.append("=== File: ").append(file.getFileName()).append(" ===\n");
                String content = readFileContent(file);
//                content = sanitizationService.sanitize(content).sanitizedText();
                analysis.append(content).append("\n\n");
            }

            return analysis.toString();

        } catch (Exception e) {
            return "Error analyzing directory: " + e.getMessage();
        }
    }

    private List<Path> findTextFiles(Path directory) throws IOException {
        List<Path> textFiles = new ArrayList<>();
        
        try (Stream<Path> paths = Files.walk(directory)) {
            textFiles = paths
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String fileName = path.toString().toLowerCase();
                    return fileName.endsWith(".txt") || 
                           fileName.endsWith(".md") || 
                           fileName.endsWith(".text");
                })
                .collect(Collectors.toList());
        }
        
        return textFiles;
    }

    private String readFileContent(Path file) throws IOException {
        return Files.readString(file);
    }
}
