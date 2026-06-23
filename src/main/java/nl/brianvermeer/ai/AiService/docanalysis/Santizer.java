package nl.brianvermeer.ai.AiService.docanalysis;

import dev.langchain4j.service.tool.ToolExecutionResult;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailRequest;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailResult.success;
import static io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailResult.successWith;

@ApplicationScoped
public class Santizer implements ToolOutputGuardrail {

    @Inject
    SanitizationService sanitizationService;

    @Override
    public ToolOutputGuardrailResult validate(ToolOutputGuardrailRequest request) {
        var sanitizationResult = sanitizationService.sanitize(request.resultText());
        if (sanitizationResult.removedInstructions().isEmpty()) {
            return success();
        }

        sanitizationResult.removedInstructions().forEach(instruction -> System.out.println("Removed instruction: " + instruction));

        System.out.println("Filtered text" + sanitizationResult.sanitizedText());
        return successWith(
            ToolExecutionResult.builder()
                    .resultText(sanitizationResult.sanitizedText())
                    .build()
        );
    }
}
