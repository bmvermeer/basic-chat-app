package nl.brianvermeer.ai.AiService.guards;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class NewInputGuard implements InputGuardrail {


    public InputGuardrailResult validate(UserMessage input) {
        System.out.println("NewInputGuard: " + input.singleText());
        return success();
    }
}
