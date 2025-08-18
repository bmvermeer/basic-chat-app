package nl.brianvermeer.ai;

import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import nl.brianvermeer.ai.AiService.PaGpt4o;
import nl.brianvermeer.ai.AiService.PaLlama31;

@WebSocket(path = "/chat")
public class ChatBot {

    private final PaGpt4o paGpt4oService;
    private final PaLlama31 paLlama31Service;
    private String currentService = "pa-gpt4o";

    public ChatBot(PaGpt4o paGpt4oService, PaLlama31 paLlama31Service) {
        this.paGpt4oService = paGpt4oService;
        this.paLlama31Service = paLlama31Service;
    }

    @OnOpen
    public String open() {
        return "How can I help?";
    }

    @OnTextMessage
    public String onTextMessage(String message) {
        if (message.startsWith("SELECT_SERVICE:")) {
            String service = message.substring("SELECT_SERVICE:".length()).trim();
            if ("pa-gpt4o".equals(service) || "pa-llama31".equals(service)) {
                currentService = service;
                return "How can I help?";
            }
        }
        
        try {
            if ("pa-gpt4o".equals(currentService)) {
                return paGpt4oService.question(message);
            } else {
                return paLlama31Service.question(message);
            }
        } catch (Exception e) {
            return "Sorry, I encountered an error: " + e.getMessage();
        }
    }

    @OnError
    public String onError(Throwable throwable) {
        System.out.println("Error: " + throwable.getMessage());
        return "ERROR: something went wrong";
    }
}
