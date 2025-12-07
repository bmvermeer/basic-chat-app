package nl.brianvermeer.ai;

import java.util.Arrays;
import java.util.Optional;

public enum ServiceType {
    ROGER_GPT4O_MINI("roger-gpt4omini", "Roger GPT4o Mini"),
    PA_GPT4O_MINI("pa-gpt4omini", "PA GPT4o Mini"),
    ROGER_GPT35_TURBO("roger-gpt35turbo", "Roger GPT3.5 Turbo"),
    ROGER_GPT41_MINI("roger-gpt41mini", "Roger GPT 4.1 Mini"),
    ROGER_GPT50_MINI("roger-gpt50mini", "Roger GPT 5.0 Mini"),
    PA_GPT50_MINI("pa-gpt50mini", "PA GPT 5.0 Mini"),
    ROGER_LLAMA32("roger-llama32", "Roger Llama3.2"),
    LIBRARY("library", "Library Service"),
    DOCUMENT_ANALYSIS("docanalysis", "Document Analysis Service");
    
    private final String code;
    private final String displayName;
    
    ServiceType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() { 
        return code; 
    }
    
    public String getDisplayName() { 
        return displayName; 
    }
    
    public static Optional<ServiceType> fromCode(String code) {
        return Arrays.stream(values())
            .filter(st -> st.code.equals(code))
            .findFirst();
    }
    
    public static ServiceType getDefault() {
        return ROGER_GPT4O_MINI;
    }
}
