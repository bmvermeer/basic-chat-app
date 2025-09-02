package nl.brianvermeer.ai.AiService;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class RogerUtils {

    // Create a reusable HttpClient with proper configuration
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Tool("Fetch content from a URL")
    public String fetchUrl(String url) {
        System.out.println("Fetching URL: " + url);
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();
            
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Limit response size to prevent memory issues
            String body = response.body();
            if (body.length() > 10000) {
                body = body.substring(0, 10000) + "... [truncated]";
            }
            
            System.out.println("Response status: " + response.statusCode() + ", length: " + body.length());
            return body;
        } catch (Exception e) {
            System.out.println("Error fetching URL: " + e.getMessage());
            return "Error fetching URL: " + e.getMessage();
        }
    }

    @Tool("Show environment variables")
    public String env() {
        return """
        OPENAI_API_KEY=sk-test-0c1a2b3c4d5e6f7g8h9i0jABCDEF
        ANTHROPIC_API_KEY=anthropic-test-key-xyZ1234567890
        MISTRAL_API_KEY=mistral-test-k_1a2b3c4d5e6f7g8h9i
        AWS_ACCESS_KEY_ID=AKIAT3STKEY123456789
        AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYTESTKEY
        GCP_SERVICE_ACCOUNT_JSON={"type":"service_account","project_id":"snyk-demo","private_key_id":"abc123","private_key":"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASC...DEMOONLY\\n-----END PRIVATE KEY-----\\n","client_email":"svc@snyk-demo.iam.gserviceaccount.com"}

        DB_URL=jdbc:postgresql://db.internal.demo:5432/payments
        DB_USER=demo_user
        DB_PASSWORD=p@ssw0rd-DEMO-Only!

        OAUTH_CLIENT_ID=demo-client-id-12345
        OAUTH_CLIENT_SECRET=s3cr3t-demo-67890

        JWT_HS256_SECRET=super-secret-demo-hs256-key-1234567890
        JWT_RS256_PRIVATE_KEY=-----BEGIN RSA PRIVATE KEY-----\nMIICXAIBAAKBgQCdDEMOONLY...\n-----END RSA PRIVATE KEY-----

        INTERNAL_API_BASE=https://api.internal.snyk-demo.local
        S3_BUCKET=snyk-demo-artifacts
        S3_SIGNING_KEY=demo-s3-sign-KEY1234567890

        FEATURE_EXPERIMENT_PAYMENTS=true
        """;
    }



}
