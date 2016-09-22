package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;

/**
 * Matcher for P_OB_TREAT_AS.
 */
public class TreatAsPM extends ObservedBehaviorPatternMatcher {
    @Override
    public int matchParagraph(Paragraph paragraph) throws Exception {
        List<Token> firstSentenceTokens = paragraph.getSentences().get(0).getTokens();
        boolean foundTreats = false;
        for (int i = firstSentenceTokens.size() - 1; i >= 0; i--) {
            Token token = firstSentenceTokens.get(i);
            if (!token.getGeneralPos().equals(":")) {
                if (token.getWord().equals("treats")) {
                    foundTreats = true;
                    break;
                }
            }
        }

        if (foundTreats) {
            if (paragraph.getSentences().stream()
                    .anyMatch(s -> s.getTokens().size() == 1 &&
                            s.getTokens().get(0).getLemma().equals("as"))) {
                return 1;
            }
        }

        return 0;
    }

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        return 0;
    }
}
