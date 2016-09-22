package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;

/**
 * Matcher for S_OB_ACTION_PAST_FINE.
 */
public class ActionPastFinePM extends ObservedBehaviorPatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();
        int verbPos = -1;

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getGeneralPos().equals("VB")) {
                if (token.getPos().equals("VBD")) {
                    verbPos = i;
                    break;
                }
            }
        }

        if (verbPos != -1) {
            if (sentence.getTokens().subList(verbPos, sentence.getTokens().size()).stream()
                    .anyMatch(t -> t.getLemma().equals("fine"))) {
                return 1;
            }
        }

        return 0;
    }
}
