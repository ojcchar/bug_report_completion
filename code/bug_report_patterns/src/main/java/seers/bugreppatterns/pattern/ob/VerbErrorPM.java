package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VerbErrorPM extends ObservedBehaviorPatternMatcher {

	final private static String[] ERROR_TERMS = { "error", "warning", "bug" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		boolean anyMatch = tokens.stream()
				.anyMatch(t -> Arrays.stream(ERROR_TERMS).anyMatch(p -> t.getLemma().equalsIgnoreCase(p)));
		if (anyMatch) {
			return 1;
		}
		return 0;
	}

}
