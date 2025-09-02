package nl.brianvermeer.ai;

import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import nl.brianvermeer.ai.AiService.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(path = "/chat/{service}")
public class ChatBot {

    private static final Logger LOG = LoggerFactory.getLogger(ChatBot.class);

    @Inject
    private ServiceRegistry serviceRegistry;

    @Inject
    WebSocketConnection connection;

    private ChatService currentService;

    public ChatBot() {
    }

    @PostConstruct
    public void init() {
        this.currentService = serviceRegistry.getDefaultService();
    }


    @OnOpen
    public String open() {
        var serviceCode = connection.pathParam("service");
        LOG.info("WebSocket connection opened for service: {}", serviceCode);
        
        var service = serviceRegistry.getService(serviceCode);
        if (service.isEmpty()) {
            LOG.error("Unknown service: {}", serviceCode);
            currentService = null;
            connection.close(); // Close the connection
            return null; // Connection will be closed
        }
        
        Object serviceObj = service.get();
        if (serviceObj instanceof ChatService) {
            currentService = (ChatService) serviceObj;
        } else {
            LOG.error("Service is not a ChatService: {}", serviceCode);
            currentService = null;
            connection.close();
            return null;
        }
        
        LOG.info("Using service: {}", serviceCode);
        return "Connected to " + serviceCode;
    }

    @OnTextMessage
    public String onTextMessage(String message) {
        if (currentService == null) {
            LOG.error("No service available");
            return "Error: No service available";
        }
        
        try {
            return currentService.question(message);
        } catch (Exception e) {
            LOG.error("Error processing message", e);
            return "Sorry, I encountered an error: " + e.getMessage();
        }
    }

    @OnError
    public String onError(Throwable throwable) {
        LOG.error("WebSocket error", throwable);
        return "ERROR: something went wrong";
    }
}
