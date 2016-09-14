package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeConditionalPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> indexes = findConditionals(tokens);
		if (indexes.size() > 0) {
			for (Integer integer : indexes) {
				if (integer > 0 && (integer < tokens.size() - 1)) {
					Sentence negclause = new Sentence(sentence.getId(), tokens.subList(0, integer));
					if(isNegative(negclause)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private List<Integer> findConditionals(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS_2, tokens);
	}
	
	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}
}
