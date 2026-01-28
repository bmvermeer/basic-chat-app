package nl.brianvermeer.ai.AiService.guards;

import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailRequest;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailResult.failure;
import static io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailResult.success;

@ApplicationScoped
public class ToolInstructionGuard implements ToolOutputGuardrail {

    @Inject
    private InstructionCheckService instructionCheckService;

    @Override
    public ToolOutputGuardrailResult validate(ToolOutputGuardrailRequest request) {
        var result = request.resultText();
        var instructionCheckResult = instructionCheckService.checkInstructions(result);
        if (!instructionCheckResult.isValid()) {
            return failure(instructionCheckResult.reason());
        }
        return success();
    }
}
