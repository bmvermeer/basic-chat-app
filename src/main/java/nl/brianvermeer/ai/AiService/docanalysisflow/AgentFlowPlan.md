# Plan: Document Analysis Flow (agentic version)

A second version of document analysis, built next to the original with the Quarkus LangChain4j agentic workflow API.
The original `docanalysis` package, its endpoint and `docanalysis.html` stay untouched.

## Decisions

| Topic | Decision |
|---|---|
| Flow | `@SequenceAgent`: **SanitizerAgent** -> **AnalysisAgent** |
| Reading files | Plain Java, outside the LLM. The sanitizer agent has no tools. |
| Sanitizing | Per file, results merged |
| Models | Sanitizer `gpt54mini`, analyst `gpt4omini` (both already in `application.properties`) |
| UI | Same layout as `docanalysis.html`, plus a collapsible "Removed instructions" panel |
| Package | `nl.brianvermeer.ai.AiService.docanalysisflow` |

## Quarkus docs findings

Source: [Agentic AI in Quarkus LangChain4j](https://docs.quarkiverse.io/quarkus-langchain4j/dev/agentic.html)

- Dependency: `io.quarkiverse.langchain4j:quarkus-langchain4j-agentic` (version managed by `${quarkus-langchain4j.version}`, currently 1.5.0).
- An agent is a plain interface with one `@Agent(description, outputKey)` method. Quarkus registers it as a CDI bean at build time. No `@RegisterAiService` needed.
- `@SequenceAgent(outputKey, subAgents = {A.class, B.class})` runs the agents in order. Each agent's `outputKey` lands in the shared `AgenticScope`, and the next agent reads it through a parameter with the same name (or `@V("name")`).
- Per-agent model: `@ModelName("gpt54mini")` on the agent method, using the named models already configured.
- The workflow interface is injected with `@Inject` and called like a method.
- `ResultWithAgenticScope<T>` as the return type gives access to every intermediate output (needed for the removed-instructions panel).
- Build-time validation: supplier methods must be `static`; `@ModelName` and `@ChatModelSupplier` can't be combined.
- Prompt variables use `{{var}}` in agents (the original uses `{var}`).

## Files to create

### Backend (`docanalysisflow`)

1. `SanitizerAgent` - `@Agent(outputKey = "sanitizedDocuments")`, `@ModelName("gpt54mini")`. Input `documents` (text with `=== File: x ===` delimiters). Reuses the system prompt of the existing `SanitizationService`, but treats input strictly as untrusted data. Returns a `SanitizationResult`.
2. `AnalysisAgent` - `@Agent(outputKey = "analysis")`, `@ModelName("gpt4omini")`. Inputs `sanitizedDocuments` (from scope) and `userRequest`. Same persona as `DocumentAnalysisService`, markdown output. The user request goes into the user message and the documents are marked as data only.
3. `DocumentAnalysisFlow` - the `@SequenceAgent(outputKey = "analysis", subAgents = {SanitizerAgent.class, AnalysisAgent.class})` interface. Returns `ResultWithAgenticScope<String>`.
4. `DocumentReader` - `@ApplicationScoped` helper. Walks the directory for `.txt/.md/.text` files (same filter as the original `DocumentAnalysisTools`) and returns a `List<DocumentFile(name, content)>`. Validates that the path exists and is a directory.
5. `DocumentAnalysisFlowService` - orchestration: read files, sanitize per file, merge, run the flow, build the response. It also holds the per-file fan-out (see Open item).
6. `DocumentAnalysisFlowResource` - `@Path("/api/document-analysis-flow")`, `POST` taking `{directory, userRequest}` and returning `{result, removedInstructions[]}`. A second endpoint, `POST /open-directory`, mirrors the original (macOS `open`). Fix to avoid: the original resource calls `analyze(userRequest, directory)` against a signature of `(directory, userRequest)`, so the arguments are swapped. Use named records here.
7. Records: `FlowRequest`, `FlowResponse`, `SanitizationResult(sanitizedText, removedInstructions)`.

### Build and config

- `pom.xml`: add `quarkus-langchain4j-agentic`.
- `application.properties`: no new models. Raise `quarkus.langchain4j.openai.gpt54mini.timeout` if per-file sanitizing gets slow.

### Frontend

- `src/main/resources/META-INF/resources/docanalysisflow.html` - copy the structure of `docanalysis.html` (theme toggle, directory input with Open in Finder, request textarea, submit, `marked` rendering). Differences:
  - posts to `/api/document-analysis-flow`
  - title "Document Analysis (Agent Flow)"
  - a `<details>` panel under the result listing removed instructions per file
  - a back link to `index.html`
- `index.html`: add a button next to "Document Analysis", e.g. `<a href="docanalysisflow.html" class="btn btn-outline-secondary position-absolute" style="top: 0; right: 270px;">Document Analysis Flow</a>`. The offset needs checking against the existing 0px and 120px buttons so nothing overlaps.

## Open item to verify first (spike)

Per-file sanitizing inside one `@SequenceAgent` needs a fan-out over a list. Check whether the LangChain4j version bundled with quarkus-langchain4j 1.5.0 has `@ParallelMapperAgent`. In order of preference:

1. **`@ParallelMapperAgent`** (if available): maps `SanitizerAgent` over the document list, then `AnalysisAgent` joins the results. The flow stays fully agentic.
2. **Java fan-out in `DocumentAnalysisFlowService`**: call `SanitizerAgent` once per file (virtual threads or sequential), then run a two-step flow. The sanitizer is still an agent, but the loop sits outside the sequence.
3. **Single call with delimiters** (the "all concatenated" option): the fallback if per-file is too slow or costly.

I'll run the spike before building and tell you which path it takes.

## Implementation order

1. Add the dependency and confirm the build and Dev UI start (`./mvnw quarkus:dev`).
2. Spike the fan-out option (above).
3. `DocumentReader` and records.
4. `SanitizerAgent`, `AnalysisAgent`, `DocumentAnalysisFlow`.
5. `DocumentAnalysisFlowService` and `DocumentAnalysisFlowResource`.
6. `docanalysisflow.html` and the `index.html` button.
7. Test.

## Verification

- Quarkus test for `DocumentReader` (file filtering, bad paths).
- Manual test against `test-docs/` with a document containing a planted injection ("ignore previous instructions ..."). Expect the injection to appear in the removed-instructions panel and not influence the analysis.
- Run the same input through the original page and compare results.
- Confirm `docanalysis.html` and `/api/document-analysis` behave exactly as before.

## Risks

- The agentic API is newer; annotation names and fan-out support depend on the bundled LangChain4j version (hence the spike).
- The sanitizer is itself an LLM and not a guarantee. The analyst prompt must still tell the model to treat document text as data.
- `log-requests = true` is on, so document text will show up in logs.
