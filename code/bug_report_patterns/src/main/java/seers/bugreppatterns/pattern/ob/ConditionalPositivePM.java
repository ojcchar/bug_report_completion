package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for OB_COND_POS.
 */
public class ConditionalPositivePM extends ObservedBehaviorPatternMatcher {

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();
        List<Integer> conditionalTermPositions = findTermsInTokens(CONDITIONAL_TERMS, tokens);

        if (conditionalTermPositions == null || conditionalTermPositions.size() < 1) {
            // If no conditional is found we know it doesn't match
            return 0;
        }

        // TODO: Make sure the sub-sentence is not EB
        // TODO: split into subsentences and only consider the one immediately following the conditional clause
        for (Integer condPos : conditionalTermPositions) {
            if (condPos + 1 < tokens.size()) {
                Sentence subSentence = new Sentence(sentence.getId(), tokens.subList(condPos + 1, tokens.size()));

                for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
                    int match = pm.matchSentence(subSentence);
                    if (match == 1) {
                        // If sub-sentence is negative, we know there is no match
                        return 0;
                    }
                }
            }

        }

        // If there's at least one conditional and none of the sub-sentences are negative
        return 1;
    }
}
