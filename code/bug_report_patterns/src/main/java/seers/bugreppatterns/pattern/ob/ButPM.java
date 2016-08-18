package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ButPM extends ObservedBehaviorPatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens2 = sentence.getTokens();
		List<Integer> buts = findTermsInTokens(CONTRAST_TERMS, tokens2);

		PatternMatcher pmw = new WorksButPM();
		int match1 = pmw.matchSentence(sentence);
		if (match1 == 0) {
			for (Integer but : buts) {
				Sentence sentence2 = new Sentence(sentence.getId(),
						tokens2.subList(but, tokens2.size()));
				for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
					int match = pm.matchSentence(sentence2);
					if (match == 0) {
						return 1;
					}
				}
			}
		}

		return 0;
	}
	
}
