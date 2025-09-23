package nl.brianvermeer.ai;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.brianvermeer.ai.AiService.ChatService;
import nl.brianvermeer.ai.AiService.PaGpt4o;
import nl.brianvermeer.ai.AiService.RogerGpt40;
import nl.brianvermeer.ai.AiService.RogerGpt35;
import nl.brianvermeer.ai.AiService.RogerGpt41;
import nl.brianvermeer.ai.AiService.RogerLlama32;
import nl.brianvermeer.ai.AiService.LibraryService;
import nl.brianvermeer.ai.AiService.docanalysis.DocumentAnalysisService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ServiceRegistry {
    
    private final Map<ServiceType, Object> services = new HashMap<>();
    private final ChatService defaultService;
    
    @Inject
    public ServiceRegistry(
            PaGpt4o paGpt4oService,
            RogerGpt40 rogerGpt40Service,
            RogerGpt35 rogerGpt35Service,
            RogerGpt41 rogerGpt41Service,
            RogerLlama32 rogerLlama32Service,
            LibraryService libraryService,
            DocumentAnalysisService documentAnalysisService) {
        
        services.put(ServiceType.PA_GPT4O_MINI, paGpt4oService);
        services.put(ServiceType.ROGER_GPT4O_MINI, rogerGpt40Service);
        services.put(ServiceType.ROGER_GPT35_TURBO, rogerGpt35Service);
        services.put(ServiceType.ROGER_GPT41_MINI, rogerGpt41Service);
        services.put(ServiceType.ROGER_LLAMA32, rogerLlama32Service);
        services.put(ServiceType.LIBRARY, libraryService);
        services.put(ServiceType.DOCUMENT_ANALYSIS, documentAnalysisService);
        
        this.defaultService = rogerGpt40Service;
    }
    
    public Optional<Object> getService(String serviceCode) {
        return ServiceType.fromCode(serviceCode)
            .map(services::get);
    }
    
    public ChatService getDefaultService() {
        return defaultService;
    }
    
    public ServiceType[] getAvailableServices() {
        return ServiceType.values();
    }
}
