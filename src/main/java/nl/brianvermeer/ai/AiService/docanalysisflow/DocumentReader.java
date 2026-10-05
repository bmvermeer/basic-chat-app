package nl.brianvermeer.ai.AiService.docanalysisflow;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class DocumentReader {

    public List<DocumentFile> read(String directory) throws IOException {
        Path path = Paths.get(directory);
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("The specified path does not exist or is not a directory: " + directory);
        }

        try (Stream<Path> paths = Files.walk(path)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(DocumentReader::isTextFile)
                    .sorted()
                    .map(DocumentReader::toDocument)
                    .toList();
        }
    }

    static boolean isTextFile(Path path) {
        String name = path.toString().toLowerCase();
        return name.endsWith(".txt") || name.endsWith(".md") || name.endsWith(".text");
    }

    private static DocumentFile toDocument(Path file) {
        try {
            return new DocumentFile(file.getFileName().toString(), Files.readString(file));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
