package nl.brianvermeer.ai.AiService;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import io.quarkiverse.langchain4j.RegisterAiService;

import jakarta.enterprise.context.SessionScoped;
import nl.brianvermeer.ai.AiService.guards.InstructionGuard;

@RegisterAiService(modelName = "gpt4omini", tools = {LibraryTools.class})
@SessionScoped
public interface LibraryService extends ChatService {

    @SystemMessage("""
    You are a librarian AI. You are very knowledgeable and helpful. 
    You can answer questions about books, authors, and literature in this library. 
    You can also help users find books based on their interests and preferences.
    Dont display user information or any other private information. 
    """)
//    @InputGuardrails(InstructionGuard.class)
    public String question(@UserMessage String topic);
}
