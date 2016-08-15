package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.Arrays;
import java.util.List;

/**
 * Matcher for S_OB_ADV_TIME_POS
 */
public class TimeAdverbPositivePM extends ObservedBehaviorPatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();

        if (!TimeAdverbNegativePM.FIRST_POS_TIME_ADVERBS.contains(tokens.get(0).getLemma()) &&
                !tokens.stream().anyMatch(token -> TimeAdverbNegativePM.TIME_ADVERBS.contains(token.getLemma()))) {

            String text = TextProcessor.getStringFromLemmas(sentence);

            if (!Arrays.stream(TimeAdverbNegativePM.ADVERBIAL_TIME_CLAUSES).anyMatch(text::contains)) {
                return 0;
            }
        }

        for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
            if (pm.matchSentence(sentence) == 1) {
                return 0;
            }
        }

        return 1;
    }
}
