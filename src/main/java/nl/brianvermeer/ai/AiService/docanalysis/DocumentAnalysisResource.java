package nl.brianvermeer.ai.AiService.docanalysis;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

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

    @POST
    @Path("/open-directory")
    public Response openDirectory(OpenDirectoryRequest request) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("open", request.directory);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                return Response.ok(new AnalysisResponse("Directory opened in Finder")).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new AnalysisResponse("Failed to open directory in Finder"))
                        .build();
            }
        } catch (IOException | InterruptedException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new AnalysisResponse("Error opening directory: " + e.getMessage()))
                    .build();
        }
    }

    public static class AnalysisRequest {
        public String directory;
        public String userRequest;
    }

    public static class OpenDirectoryRequest {
        public String directory;
    }

    public static class AnalysisResponse {
        public String result;

        public AnalysisResponse(String result) {
            this.result = result;
        }
    }
}
