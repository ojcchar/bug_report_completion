package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class FrequencyAdverbPM extends ObservedBehaviorPatternMatcher {

	final public static String[] FREQ_ADV = { "always", "never", "sometimes", "forever", "occasionally", "often" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> verbs = findVerbs(tokens);
		for (Integer verb : verbs) {
			if (verb + 1 < tokens.size()) {
				Token nextToken = tokens.get(verb + 1);
				if (Arrays.stream(FREQ_ADV).anyMatch(t -> nextToken.getLemma().equals(t))) {
					return 1;
				}
			}

			if (verb - 1 >= 0) {
				Token prevToken = tokens.get(verb - 1);
				if (Arrays.stream(FREQ_ADV).anyMatch(t -> prevToken.getLemma().equals(t))) {
					return 1;
				}
			}
		}
		return 0;
	}

	private List<Integer> findVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {

				verbs.add(i);
			}
		}
		return verbs;
	}

}
