package nl.brianvermeer.ai.AiService.guards;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrail;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.brianvermeer.ai.AiService.guards.InstructionCheckService.InstructionCheckResult;

@ApplicationScoped
public class InstructionGuard implements InputGuardrail{

    @Inject
    private InstructionCheckService instructionCheckService;

    public InputGuardrailResult validate(UserMessage input) {

        InstructionCheckResult instructionCheckResult = instructionCheckService.checkInstructions(input.singleText());
        System.out.println("instructionCheckResult: " + instructionCheckResult);
        if (!instructionCheckResult.isValid()) {
            return failure(instructionCheckResult.reason());
        } 

        return success();
    }
}
