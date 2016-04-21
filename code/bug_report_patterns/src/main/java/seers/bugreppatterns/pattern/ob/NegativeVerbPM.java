package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeVerbPM extends ObservedBehaviorPatternMatcher {

	final private static String[] NEGATIVE_VERBS = { "fail", "freeze", "hang", "disappear", "prohibit", "ignore",
			"break", "bypass", "lose", "wipe", "interfere", "vanish", "stop", "truncate", "offset", "complain",
			"delete", "remain", "cut", "crash", "forget", "block", "interrupt", "close", "failes", "overlap", "kill",
			"lack", "shift", "retain", "stick", "duplicate" };

	final private static String[] OTHER_NEGATIVE_VERBS = { "strip away", "slow doen", "slow down", "get stick",
			"stick up", "faile", "stucks up", "consume 100", "stick in", "get turn into", "output null", "be out of",
			"pull out", "faul", "hangs/get" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		boolean anyMatch = tokens.stream()
				.anyMatch(t -> Arrays.stream(NEGATIVE_VERBS).anyMatch(p -> t.getLemma().equalsIgnoreCase(p)));
		if (anyMatch) {
			return 1;
		}

		String txt = TextProcessor.getStringFromLemmas(sentence);

		if (Arrays.stream(OTHER_NEGATIVE_VERBS).anyMatch(p -> txt.contains(p))) {
			return 1;
		}

		return 0;
	}

}
