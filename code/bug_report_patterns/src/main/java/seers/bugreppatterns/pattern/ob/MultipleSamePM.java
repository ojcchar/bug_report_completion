package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class MultipleSamePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// no verbs
		List<Token> tokens = sentence.getTokens();
		if (tokens.stream().anyMatch(tok -> tok.getGeneralPos().equals("VB"))) {
			return 0;
		}

		// no multiple
		if (!tokens.get(0).getLemma().equals("multiple")) {
			return 0;
		}

		// no "same" after the "multiple"
		if (tokens.subList(1, tokens.size()).stream().noneMatch(tok -> tok.getLemma().equals("same"))) {
			return 0;
		}

		return 1;
	}

}
