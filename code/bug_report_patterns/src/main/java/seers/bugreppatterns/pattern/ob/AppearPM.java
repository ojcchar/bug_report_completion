package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AppearPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.matches(".*(appear|seem) (to|that|like|each|as|none).*")) {
			return 1;
		}

		List<Token> tokens = sentence.getTokens();
		List<Integer> terms = findMainTerms(tokens);

		for (Integer term : terms) {
			if (term + 1 < tokens.size()) {
				Token nextToken = tokens.get(term + 1);
				if (nextToken.getGeneralPos().equals("NN")) {
					return 1;
				}
			}
		}
		return 0;
	}

	private List<Integer> findMainTerms(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if ((token.getLemma().equals("seem")) || (token.getLemma().equals("appear"))) {
				idxs.add(i);
			}
		}
		return idxs;
	}
}
