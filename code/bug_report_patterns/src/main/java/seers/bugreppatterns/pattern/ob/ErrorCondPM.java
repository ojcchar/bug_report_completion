package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorCondPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ProblemInPM(), new NoNounPM(), new ErrorNounPhrasePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> condIndexes = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, tokens);
		if (!condIndexes.isEmpty()) {
			for (Integer condIndex : condIndexes) {
				if (condIndex > 0 && (condIndex < tokens.size() - 1)) {
					Sentence errorClause = new Sentence(sentence.getId(), tokens.subList(0, condIndex));
					if (isNegative(errorClause)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
