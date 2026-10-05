package nl.brianvermeer.ai.AiService.docanalysisflow;

import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DocumentAnalysisFlowService {

    @Inject
    DocumentReader documentReader;

    @Inject
    DocumentAnalysisFlow flow;

    public FlowResponse analyze(String directory, String userRequest) throws IOException {
        List<DocumentFile> files = documentReader.read(directory);
        if (files.isEmpty()) {
            return FlowResponse.error("No text files found in directory: " + directory);
        }

        String documents = files.stream()
                .map(f -> "=== File: " + f.name() + " ===\n" + f.content())
                .collect(Collectors.joining("\n\n"));

        ResultWithAgenticScope<String> result = flow.analyze(documents, userRequest);

        SanitizationResult sanitized = result.agenticScope().readState("sanitizedDocuments", new SanitizationResult(List.of()));
        return new FlowResponse(result.result(), sanitized.documents());
    }
}
