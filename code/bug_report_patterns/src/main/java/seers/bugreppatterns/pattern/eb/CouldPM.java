package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CouldPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getGeneralPos().equals("MD") && (tokens.get(i).getLemma().equals("could"))) {
				PatternMatcher pm = new CouldQuestionSentencePM();
				int match = pm.matchSentence(sentence);
				if (match == 0) {
					return 1;
				}
			}
		}

		return 0;
	}

}
