package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ForTimePM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> FOR_TERMS = JavaUtils.getSet("for", "since", "take");

	private final static Set<String> OTHER_TIME_TERMS = JavaUtils.getSet("monday", "tuesday", "wednesday", "thursday",
			"friday", "saturday", "sunday", "mon", "tue", "wed", "thu", "fri", "sat", "sun", "january", "february",
			"march", "april", "may", "june", "july", "august", "september", "october", "november", "december", "jan",
			"feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Integer> fors = SentenceUtils.findLemmasInTokens(FOR_TERMS, sentence.getTokens());

		if (!fors.isEmpty()) {

			// split sentences based on "for"
			List<Sentence> subSentences = SentenceUtils.findSubSentences(sentence, fors);

			for (int i = fors.get(0) == 0 ? 0 : 1; i < subSentences.size(); i++) {
				// The right for: the one that precedes a time term
				if (containsTimeTerms(subSentences.get(i).getTokens())) {
					return 1;
				}
			}
		}

		return 0;
	}

	private boolean containsTimeTerms(List<Token> tokens) {
		return SentenceUtils.tokensContainAnyLemmaIn(tokens, AfterTimePM.TIME_TERMS)
				|| SentenceUtils.tokensContainAnyLemmaIn(tokens, OTHER_TIME_TERMS);
	}

}
