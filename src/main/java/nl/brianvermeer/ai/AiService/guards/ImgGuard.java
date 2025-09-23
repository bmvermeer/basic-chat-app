package nl.brianvermeer.ai.AiService.guards;


import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImgGuard implements OutputGuardrail{

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
