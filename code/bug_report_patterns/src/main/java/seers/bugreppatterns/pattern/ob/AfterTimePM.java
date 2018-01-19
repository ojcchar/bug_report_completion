package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AfterTimePM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> AFTER = JavaUtils.getSet("after");

	public final static Set<String> TIME_TERMS = JavaUtils.getSet("second", "seconds", "sec", "secs", "minute", "min",
			"mins", "hour", "day", "week", "month", "year");

	private static final Set<String> PUNCTUATION = JavaUtils.getSet(",", "_", "-");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// split sentences based on "."
		List<Sentence> superSentences = SentenceUtils.breakByParenthesis(sentence);

		for (Sentence superSentence : superSentences) {

			List<Integer> afters = SentenceUtils.findLemmasInTokens(AFTER, superSentence.getTokens());

			if (!afters.isEmpty()) {
				// split sentences based on "after"
				List<Sentence> subSentences = SentenceUtils.findSubSentences(superSentence, afters);

				// if there is a sentence before the "after" term, skip it ->
				// the focus is on what is after the "after"
				for (int i = afters.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {
					Sentence subSentece = subSentences.get(i);

					List<Integer> punct = findPunctuation(subSentece.getTokens());

					// hard case: there is no punctuation. Check for time terms
					// and verify there is no verb before them.

					if (punct.isEmpty()) {
						List<Integer> timeTokens = SentenceUtils.findLemmasInTokens(TIME_TERMS, subSentece.getTokens());
						if (!timeTokens.isEmpty()) {
							if (!containsVerb(subSentece.getTokens().subList(0, timeTokens.get(0))))
								return 1;
						}

					}
					// The easy case: there is punctuation (',', '_', '-'). Make
					// sure that the sentence between the
					// "after" and punctuation contains time terms and that
					// there are no verbs before them.
					else {
						List<Sentence> subSubSentences = SentenceUtils.findSubSentences(subSentece, punct);

						if (!subSubSentences.isEmpty()) {
							List<Integer> timeTokens = SentenceUtils.findLemmasInTokens(TIME_TERMS,
									subSubSentences.get(0).getTokens());
							if (!timeTokens.isEmpty()) {
								if (!containsVerb(subSubSentences.get(0).getTokens().subList(0, timeTokens.get(0))))
									return 1;
							}
						}
					}

				}
			}
		}
		return 0;
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
		if (symbols.size() - 1 >= 0 && symbols.get(symbols.size() - 1) == tokens.size() - 1) {
			return symbols.subList(0, symbols.size() - 1);
		}
		return symbols;
	}

	private boolean containsVerb(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				return true;
			}
		}
		return false;
	}

}
