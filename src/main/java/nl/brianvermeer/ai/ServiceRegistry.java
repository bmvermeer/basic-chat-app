package nl.brianvermeer.ai;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.brianvermeer.ai.AiService.ChatService;
import nl.brianvermeer.ai.AiService.PaGpt4o;
import nl.brianvermeer.ai.AiService.PaLlama31;
import nl.brianvermeer.ai.AiService.PaLlama32;
import nl.brianvermeer.ai.AiService.RogerGpt40;
import nl.brianvermeer.ai.AiService.RogerGpt35;
import nl.brianvermeer.ai.AiService.RogerGpt41;
import nl.brianvermeer.ai.AiService.RogerLlama31;
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
            PaLlama31 paLlama31Service,
            PaLlama32 paLlama32Service,
            RogerGpt40 rogerGpt40Service,
            RogerGpt35 rogerGpt35Service,
            RogerGpt41 rogerGpt41Service,
            RogerLlama31 rogerLlama31Service,
            LibraryService libraryService,
            DocumentAnalysisService documentAnalysisService) {
        
        services.put(ServiceType.PA_GPT4O_MINI, paGpt4oService);
        services.put(ServiceType.PA_LLAMA31, paLlama31Service);
        services.put(ServiceType.PA_LLAMA32, paLlama32Service);
        services.put(ServiceType.ROGER_GPT4O_MINI, rogerGpt40Service);
        services.put(ServiceType.ROGER_GPT35_TURBO, rogerGpt35Service);
        services.put(ServiceType.ROGER_GPT41_MINI, rogerGpt41Service);
        services.put(ServiceType.ROGER_LLAMA31, rogerLlama31Service);
        services.put(ServiceType.LIBRARY, libraryService);
        services.put(ServiceType.DOCUMENT_ANALYSIS, documentAnalysisService);
        
        this.defaultService = paGpt4oService;
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
