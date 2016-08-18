package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeVerbPM extends ObservedBehaviorPatternMatcher {

	final private static String[] OTHER_NEGATIVE_VERBS = { "slow doen", "slow down", "faile", "stucks up",
			"consume 100", "get turn into", "be out of", "pull out", "faul", "hangs/get", "failes", "timing out",
			"go away" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		for (Token token : tokens) {
			if (Arrays.stream(NegativeTerms.VERBS).anyMatch(t -> token.getLemma().startsWith(t))
					&& (token.getGeneralPos().equals("VB") || token.getGeneralPos().equals("NN")
							|| token.getGeneralPos().equals("JJ"))) {
				return 1;
			}
		}
		String txt = TextProcessor.getStringFromLemmas(sentence);

		if (Arrays.stream(OTHER_NEGATIVE_VERBS).anyMatch(p -> txt.contains(p))) {
			return 1;
		}

		return 0;
	}

}
