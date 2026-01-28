package nl.brianvermeer.ai.AiService.guards;


import dev.langchain4j.data.message.AiMessage;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImgGuard implements OutputGuardrail {

    public OutputGuardrailResult validate(AiMessage responseFromLLM) {

        String text = responseFromLLM.text();
        System.out.println("text: " + text);
        
        if (text.contains("![")) {
            var newtext = text.replaceAll("!\\[([^\\]]*)\\]\\([^)]*\\)", "[IMAGE BLOCKED: $1]");
            return successWith(newtext);
        }
        
        return success();
    }

}
