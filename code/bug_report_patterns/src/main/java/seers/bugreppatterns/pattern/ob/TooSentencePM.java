package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class TooSentencePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> terms = findMainTerms(tokens);
		for (Integer tooTerm : terms) {
			if (tooTerm + 1 < tokens.size()) {
				Token nextToken = tokens.get(tooTerm + 1);
				if (nextToken.getGeneralPos().equals("JJ") || nextToken.getGeneralPos().equals("RB")) {
					return 1;
				}
			}
		}

		String txt = TextProcessor.getStringFromLemmas(sentence);

		if (txt.matches(".*(too-large|to long).*")) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findMainTerms(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("too") && token.getGeneralPos().equals("RB")) {
				idxs.add(i);
			}
		}
		return idxs;
	}
}
