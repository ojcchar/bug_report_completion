package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_OB_ADV_TIME_POS
 */
public class TimeAdverbPositivePM extends ObservedBehaviorPatternMatcher {
	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> subSentences = SentenceUtils.breakByParenthesis(sentence);

		for (Sentence subSentence : subSentences) {
			List<Token> tokens = subSentence.getTokens();

			if (tokens.isEmpty()) {
				continue;
			}

			boolean firstTokenTimeAdverb = SentenceUtils.lemmasContainToken(TimeAdverbNegativePM.FIRST_POS_TIME_ADVERBS,
					tokens.get(0));
			boolean containsTimeAdverb = SentenceUtils.sentenceContainsAnyLemmaIn(subSentence,
					TimeAdverbNegativePM.TIME_ADVERBS);
			boolean matchesAdverbialClauses = TextProcessor.getStringFromLemmas(subSentence)
					.matches(".*" + TimeAdverbNegativePM.ADVERBIAL_TIME_CLAUSES + ".*");

			if (firstTokenTimeAdverb || containsTimeAdverb || matchesAdverbialClauses) {
				if (!isNegative(subSentence)) {
					return 1;
				}
			}
		}
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		PatternMatcher pm = findFirstPatternThatMatches(sentence, ButNegativePM.NEGATIVE_PMS);
		// if (pm != null) {
		// System.out.println(pm.getClass().getSimpleName());
		// }
		return pm != null;
	}
}
