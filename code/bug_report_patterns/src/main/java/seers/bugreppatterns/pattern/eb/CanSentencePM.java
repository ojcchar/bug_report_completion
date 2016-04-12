package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CanSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		Token firstToken = tokens.get(0);
		Token lastToken = tokens.get(tokens.size() - 1);
		if (firstToken.getLemma().equalsIgnoreCase("can") && lastToken.getLemma().equalsIgnoreCase("?")) {
			return 0;
		}

		boolean anyMatch = tokens.stream()
				.anyMatch(t -> t.getPos().equals("MD") && t.getLemma().equalsIgnoreCase("can"));
		return anyMatch ? 1 : 0;
	}

}
