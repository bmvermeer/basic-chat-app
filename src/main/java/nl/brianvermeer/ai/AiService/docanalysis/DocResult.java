package nl.brianvermeer.ai.AiService.docanalysis;

public record DocResult(String title, int score, String verdict) {

    @Override
    public String toString() {
        return "### Title: " + title + "\n" +
                "- Score: " + score + "\n" +
                "- Verdict: " + verdict;
    }
}
