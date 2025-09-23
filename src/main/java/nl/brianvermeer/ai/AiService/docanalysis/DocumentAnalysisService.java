package nl.brianvermeer.ai.AiService.docanalysis;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;
import jakarta.enterprise.context.RequestScoped;
import nl.brianvermeer.ai.AiService.guards.ImgGuard;

@RegisterAiService(modelName = "gpt4omini", tools = {DocumentAnalysisTools.class})
@RequestScoped
public interface DocumentAnalysisService {

    @SystemMessage("""
    You are an expert document analyst AI. You analyze directories of text documents based on user requests.
    You provide thoughtful, detailed analysis and actionable insights about the documents found.
    Always be helpful and professional in your responses.
    """)
    @UserMessage("""
    Analyze the following documents directory:
    {directory}
    Based on the user request:
    {userRequest}
    Output the analysis in markdown format.
    """)
    // @OutputGuardrails(ImgGuard.class)
    public String analyze(String directory, String userRequest);
}
