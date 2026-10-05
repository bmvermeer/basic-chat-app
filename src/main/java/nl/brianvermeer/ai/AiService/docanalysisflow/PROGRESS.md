# Progress: Document Analysis Flow

Plan: `AgentFlowPlan.md` (same folder). Update this file after every step. To resume, continue at the first unchecked item.

- [x] 1. Add `quarkus-langchain4j-agentic` to pom.xml, confirm it compiles
- [x] 2. Spike: fan-out support (`@ParallelMapperAgent`?) in bundled LangChain4j
- [x] 3. `DocumentReader` and records
- [x] 4. `SanitizerAgent`, `AnalysisAgent`, `DocumentAnalysisFlow`
- [x] 5. `DocumentAnalysisFlowService`, `DocumentAnalysisFlowResource`
- [x] 6. `docanalysisflow.html` and `index.html` button
- [x] 7. Tests / verification

## Notes
- Spike result: bundled langchain4j-agentic is 1.9.1-beta17, no `@ParallelMapperAgent`. Used plan fallback 3: one SanitizerAgent call over all documents (delimited by `=== File: x ===`), which returns a per-file `SanitizationResult`. So the UI still shows removals per file.
- Agentic extension needs a default chat model; added `quarkus.langchain4j.chat-model.provider=openai` + default openai key/model at the end of application.properties (otherwise the build fails).
- Backend done and `./mvnw package` succeeds. Not yet run against OpenAI (no live test).
- Frontend done. Reader unit test passes. Live run via curl (OpenAI) confirmed: planted injection in bad.txt was removed and reported per file, analysis ignored it. DONE.
