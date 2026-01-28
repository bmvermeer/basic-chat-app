package nl.brianvermeer.ai.AiService;

import dev.langchain4j.agent.tool.Tool;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import nl.brianvermeer.ai.AiService.guards.ToolInstructionGuard;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class Utils {

//    @ToolOutputGuardrails(ToolInstructionGuard.class)
    @Tool("Fetch content from a URL")
    public String fetchUrl(String url) {
        System.out.println("Fetching URL: " + url);
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());
            return response.body();
        } catch (Exception e) {
            return "Error fetching URL: " + e.getMessage();
        }
    }

    @POST

    @Tool("Execute a terminal command")
    public String executeTerminalCommand(String command) {
        System.out.println("Executing terminal command: " + command);
        try {
            var process = new ProcessBuilder()
                    .command("sh", "-c", command)
                    .redirectErrorStream(true)
                    .start();
            
            var output = new String(process.getInputStream().readAllBytes());
            process.waitFor();
            
            return output.trim();
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }

    @Tool("Download a file from URL to /download directory")
    public String downloadFile(String url) {
        System.out.println("Downloading file from: " + url);
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            
            // Extract filename from URL
            var fileName = url.substring(url.lastIndexOf('/') + 1);
            if (fileName.isEmpty() || !fileName.contains(".")) {
                fileName = "downloaded_file";
            }
            
            // Create download directory if it doesn't exist
            var downloadDir = java.nio.file.Path.of("/download");
            java.nio.file.Files.createDirectories(downloadDir);
            
            // Write file
            var filePath = downloadDir.resolve(fileName);
            java.nio.file.Files.write(filePath, response.body());
            
            return "File downloaded successfully to: " + filePath;
        } catch (Exception e) {
            return "Error downloading file: " + e.getMessage();
        }
    }
}
