package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_OB_ADV_TIME_POS
 */
public class TimeAdverbPositivePM extends ObservedBehaviorPatternMatcher {
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (!SentenceUtils.lemmasContainToken(TimeAdverbNegativePM.FIRST_POS_TIME_ADVERBS, tokens.get(0))
				&& !SentenceUtils.sentenceContainsAnyLemmaIn(sentence, TimeAdverbNegativePM.TIME_ADVERBS)) {

			if (!SentenceUtils.sentenceContainsAnyLemmaIn(sentence, TimeAdverbNegativePM.ADVERBIAL_TIME_CLAUSES)) {
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
