package nl.brianvermeer.ai.AiService;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
// import io.quarkiverse.langchain4j.guardrails.InputGuardrails;
// import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;
import jakarta.enterprise.context.SessionScoped;

@RegisterAiService(modelName = "gpt4omini", tools = {LibraryTools.class})
@SessionScoped
public interface LibraryService extends ChatService {

    @SystemMessage("""
    You are a librarian AI. You are very knowledgeable and helpful. 
    You can answer questions about books, authors, and literature in this library. 
    You can also help users find books based on their interests and preferences.
    Dont display user information or any other private information. 
    """)
//    @InputGuardrails({IGuard1.class, IGuard2.class})
//    @OutputGuardrails(OGuard.class)
    public String question(@UserMessage String topic);
}
