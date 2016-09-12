package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AfterTimePM extends ObservedBehaviorPatternMatcher {

	public final static String[] AFTER = { "after" };

	public final static String[] TIME_TERMS = { "second", "seconds", "sec", "secs", "minute", "min", "mins", "hour",
			"day", "week", "month", "year" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		// split sentences based on "."
		List<Sentence> superSentences = findSubSentences(sentence, findPeriod(tokens));

		for (Sentence superSentence : superSentences) {

			List<Integer> afters = findLemmasInTokens(AFTER, superSentence.getTokens());

			if (!afters.isEmpty()) {
				// split sentences based on "after"
				List<Sentence> subSentences = findSubSentences(superSentence, afters);

				// if there is a sentence before the "after" term, skip it -> the focus is on what is after the "after"
				for (int i = afters.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {
					Sentence subSentece = subSentences.get(i);

					List<Integer> punct = findPunctuation(subSentece.getTokens());

					// hard case: there is no punctuation. Check for time terms and verify there is no verb before them.

					if (punct.isEmpty()) {
						List<Integer> timeTokens = findLemmasInTokens(TIME_TERMS, subSentece.getTokens());
						if (!timeTokens.isEmpty()) {
							if (!containsVerb(subSentece.getTokens().subList(0, timeTokens.get(0))))
								return 1;
						}

					}
					// The easy case: there is punctuation (',', '_', '-'). Make sure that the sentence between the
					// "after" and punctuation contains time terms and that there are no verbs before them.
					else {
						List<Sentence> subSubSentences = findSubSentences(subSentece, punct);

						if (!subSubSentences.isEmpty()) {
							List<Integer> timeTokens = findLemmasInTokens(TIME_TERMS,
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
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals(",") || token.getWord().equals("_") || token.getWord().equals("-")) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	private List<Integer> findPeriod(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		LinkedList<Character> pars = new LinkedList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals("-LRB-")) {
				pars.add('(');
			}
			if (token.getWord().equals("-RRB-")) {
				pars.removeLast();
			}
			if (token.getWord().equals(".") && pars.isEmpty()) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	private boolean containsVerb(List<Token> tokens) {
		LinkedList<Character> pars = new LinkedList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals("-LRB-")) {
				pars.add('(');
			}
			if (token.getWord().equals("-RRB-")) {
				pars.removeLast();
			}
			if (token.getGeneralPos().equals("VB") && pars.isEmpty()) {
				return true;
			}
		}
		return false;
	}

}
