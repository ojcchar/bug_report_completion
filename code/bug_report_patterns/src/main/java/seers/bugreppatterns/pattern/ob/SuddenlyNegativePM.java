package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SuddenlyNegativePM extends ObservedBehaviorPatternMatcher {

	public final static String[] SUDDEN_TERMS = { "sudden", "suddenly"};
	
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		
		List<Integer> sudden = findSuddenTerms(tokens);

		if (!sudden.isEmpty() && isNegative(sentence)) {
			return 1;
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NegativeAfterPM.NEGATIVE_PMS);
	}

	private List<Integer> findSuddenTerms(List<Token> tokens) {
		return findLemmasInTokens(SUDDEN_TERMS, tokens);
	}

}
