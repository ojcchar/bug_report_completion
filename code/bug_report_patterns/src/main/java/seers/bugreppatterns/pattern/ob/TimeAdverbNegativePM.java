package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
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

	public static final String ADVERBIAL_TIME_CLAUSES = "(as soon as|right now|once again|for the moment|up to now|as it stand|for a brief moment|for a moment|in this moment|every time)";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> subSentences = SentenceUtils.breakByParenthesis(sentence);

		for (Sentence subSentence : subSentences) {
			List<Token> tokens = subSentence.getTokens();

			if (tokens.isEmpty()) {
				continue;
			}
			if (SentenceUtils.lemmasContainToken(FIRST_POS_TIME_ADVERBS, tokens.get(0))
					|| SentenceUtils.sentenceContainsAnyLemmaIn(subSentence, TIME_ADVERBS)
					|| TextProcessor.getStringFromLemmas(subSentence).matches(".*" + ADVERBIAL_TIME_CLAUSES + ".*")) {

				if (isNegative(subSentence)) {
					return 1;
				}
			}
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}
}
