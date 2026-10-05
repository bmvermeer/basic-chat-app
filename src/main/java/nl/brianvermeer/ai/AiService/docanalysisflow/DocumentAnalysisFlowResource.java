package nl.brianvermeer.ai.AiService.docanalysisflow;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@Path("/api/document-analysis-flow")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentAnalysisFlowResource {

    @Inject
    DocumentAnalysisFlowService service;

    @POST
    public Response analyze(FlowRequest request) {
        try {
            return Response.ok(service.analyze(request.directory(), request.userRequest())).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(FlowResponse.error("Error: " + e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(FlowResponse.error("Error: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/open-directory")
    public Response openDirectory(OpenDirectoryRequest request) {
        try {
            Process process = new ProcessBuilder("open", request.directory()).start();
            if (process.waitFor() == 0) {
                return Response.ok(FlowResponse.error("Directory opened in Finder")).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(FlowResponse.error("Failed to open directory in Finder"))
                    .build();
        } catch (IOException | InterruptedException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(FlowResponse.error("Error opening directory: " + e.getMessage()))
                    .build();
        }
    }

    public record OpenDirectoryRequest(String directory) {
    }
}
