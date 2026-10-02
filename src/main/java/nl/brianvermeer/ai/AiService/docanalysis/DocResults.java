package nl.brianvermeer.ai.AiService.docanalysis;

import java.util.List;
import java.util.stream.Collectors;

public record DocResults(List<DocResult> docResultList) {
    @Override
    public String toString() {
        return docResultList.stream().map(Record::toString).collect(Collectors.joining("\n\n"));
    }

}
