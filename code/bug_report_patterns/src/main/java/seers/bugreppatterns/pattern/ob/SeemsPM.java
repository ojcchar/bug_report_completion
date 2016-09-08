package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SeemsPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] OTHER_SEEM = { new SeemsToNegativeVerbPM(), new SeemsToBePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// Check that is not other seem pattern
		if(isOtherSeem(sentence)) {
			return 0;
		}

		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.matches(".*(appear|seem|look)(ed|s|ing)? (to|that|like|each|as|none)[^A-Za-z].*")) {
			return 1;
		}

		List<Token> tokens = sentence.getTokens();
		List<Integer> terms = findMainTerms(tokens);

		for (Integer term : terms) {
			if (term + 1 < tokens.size()) {
				Token nextToken = tokens.get(term + 1);
				if (nextToken.getGeneralPos().equals("NN") || nextToken.getGeneralPos().equals("RB")
						|| nextToken.getGeneralPos().equals("JJ") || nextToken.getGeneralPos().equals("DT")) {
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
			if ((token.getLemma().equals("seem")) || (token.getLemma().equals("appear"))
					|| (token.getLemma().equals("look"))) {
				idxs.add(i);
			}
		}
		return idxs;
	}
	
	private boolean isOtherSeem(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, OTHER_SEEM);
	}
}
