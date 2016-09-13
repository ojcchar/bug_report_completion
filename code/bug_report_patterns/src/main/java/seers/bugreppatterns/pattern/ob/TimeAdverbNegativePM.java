package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_OB_ADV_TIME_NEG.
 */
public class TimeAdverbNegativePM extends ObservedBehaviorPatternMatcher {
	public static final Set<String> TIME_ADVERBS = JavaUtils.getSet("currently", "again", "now");

	/**
	 * Adverbs that only count as a match if they are in the first position.
	 */
	public static final Set<String> FIRST_POS_TIME_ADVERBS = JavaUtils.getSet("then", "now", "again");

	public static final Set<String> ADVERBIAL_TIME_CLAUSES = JavaUtils.getSet("as soon as", "right now", "once again",
			"for the moment", "up to now", "as it stand", "for a brief moment", "for a moment", "in this moment");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		if (!SentenceUtils.lemmasContainToken(FIRST_POS_TIME_ADVERBS, tokens.get(0))
				&& !SentenceUtils.sentenceContainsAnyLemmaIn(sentence, TIME_ADVERBS)) {

			if (!SentenceUtils.sentenceContainsAnyLemmaIn(sentence, ADVERBIAL_TIME_CLAUSES)) {
				return 0;
			}
		}

		if (isNegative(sentence)) {
			return 1;
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}
}
