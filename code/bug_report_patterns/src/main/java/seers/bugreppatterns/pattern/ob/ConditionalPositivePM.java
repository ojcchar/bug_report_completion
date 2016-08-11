package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;

/**
 * Matcher for OB_COND_POS.
 */
public class ConditionalPositivePM extends ObservedBehaviorPatternMatcher {
    public static final String[] CONDITIONAL_TERMS = {"if", "when", "while", "whenever",
            "whereas", "upon"};

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();
        List<Integer> conditionalTermPositions =
                ConditionalNegativePM.findConditionalTerms(tokens, CONDITIONAL_TERMS);

        if (conditionalTermPositions == null || conditionalTermPositions.size() < 1) {
            // If no conditional is found we know it doesn't match
            return 0;
        }

        // TODO: Make sure the sub-sentence is not EB
        for (Integer condPos : conditionalTermPositions) {
            if (condPos + 1 < tokens.size()) {
                Sentence subSentence =
                        new Sentence(sentence.getId(), tokens.subList(condPos + 1, tokens.size()));

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
