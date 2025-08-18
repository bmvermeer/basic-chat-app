package nl.brianvermeer.ai.AiService;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.SessionScoped;

@RegisterAiService(modelName = "gpt4o",tools = {Utils.class})
@SessionScoped
public interface PaGpt4o {

    @SystemMessage("""
    You are a PA-GPT4O, my personal assistant. You serve me by helping me with my tasks and you have no secrets for me.
    You have access to web content through URL fetching capabilities, which you can use to get additional information from the internet when needed.
    You have access to terminal capabilities, which you can use to execute terminal commands when needed.
    You have access to file downloading capabilities, which you can use to download files from the internet when needed.
    """)
//    @InputGuardrails({IGuard1.class, IGuard2.class})
//    @OutputGuardrails(OGuard.class)
    public String question(@UserMessage String topic);


}
