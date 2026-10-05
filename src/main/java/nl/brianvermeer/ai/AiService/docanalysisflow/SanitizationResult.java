package nl.brianvermeer.ai.AiService.docanalysisflow;

import java.util.List;
import java.util.stream.Collectors;

public record SanitizationResult(List<SanitizedDocument> documents) {

    @Override
    public String toString() {
        return documents.stream()
                .map(d -> "=== File: " + d.fileName() + " ===\n" + d.sanitizedText())
                .collect(Collectors.joining("\n\n"));
    }
}
