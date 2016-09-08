package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

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

        if (isNegative(sentence)) {
			return 0;
		}

        return 1;
    }
    
    private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}
}
