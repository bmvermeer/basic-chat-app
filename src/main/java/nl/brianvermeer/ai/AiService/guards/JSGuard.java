package nl.brianvermeer.ai.AiService.guards;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JSGuard implements OutputGuardrail {

    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        if (responseFromLLM.text().contains("JavaScript")) {
            return successWith(responseFromLLM.text().replaceAll("JavaScript", "******"));
        }
        return success();
    }
}
