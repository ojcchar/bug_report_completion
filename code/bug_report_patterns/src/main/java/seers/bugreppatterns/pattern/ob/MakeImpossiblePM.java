package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class MakeImpossiblePM extends ObservedBehaviorPatternMatcher {

	private static final String MAKE = "make";
	private static final String IMPOSSIBLE = "impossible";

	private final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		int makeIndex = findMakeIndex(tokens);

		if (makeIndex > -1 && containsImpossible(tokens.subList(makeIndex + 1, tokens.size())) && !isEBModal(tokens)
				&& !isNegative(sentence)) {
			return 1;
		}
		return 0;
	}

	private boolean containsImpossible(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getGeneralPos().equals("JJ") && current.getLemma().equals(IMPOSSIBLE)) {
				return true;
			}
		}
		return false;
	}

	private int findMakeIndex(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			if (current.getGeneralPos().equals("VB") && current.getLemma().equals(MAKE)) {
				return i;
			}
		}
		return -1;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
