package nl.brianvermeer.ai.AiService.docanalysis;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/document-analysis")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentAnalysisResource {

    @Inject
    DocumentAnalysisService documentAnalysisService;

    @POST
    public Response analyzeDocuments(AnalysisRequest request) {
        try {
            String result = documentAnalysisService.analyze(request.userRequest, request.directory);
            return Response.ok(new AnalysisResponse(result)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new AnalysisResponse("Error: " + e.getMessage()))
                    .build();
        }
    }

    public static class AnalysisRequest {
        public String directory;
        public String userRequest;
    }

    public static class AnalysisResponse {
        public String result;

        public AnalysisResponse(String result) {
            this.result = result;
        }
    }
}
