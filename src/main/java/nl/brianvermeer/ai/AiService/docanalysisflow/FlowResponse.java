package nl.brianvermeer.ai.AiService.docanalysisflow;

import java.util.List;

public record FlowResponse(String result, List<SanitizedDocument> sanitizedDocuments) {

    public static FlowResponse error(String message) {
        return new FlowResponse(message, List.of());
    }
}
