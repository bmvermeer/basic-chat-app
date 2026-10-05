package nl.brianvermeer.ai.AiService.docanalysisflow;

import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;

public interface DocumentAnalysisFlow {

    @SequenceAgent(outputKey = "analysis", subAgents = {SanitizerAgent.class, AnalysisAgent.class})
    ResultWithAgenticScope<String> analyze(String documents, String userRequest);
}
