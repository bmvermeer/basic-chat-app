package nl.brianvermeer.ai.AiService.docanalysisflow;

import java.util.List;

public record SanitizedDocument(String fileName, String sanitizedText, List<String> removedInstructions) {
}
