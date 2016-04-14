package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VerbNoPM extends ObservedBehaviorPatternMatcher {

	final private static String[] NEGATIVE_TERMS = { "no", "nothing"
			// ,"not", "never"
	};

	final private static String[] ADDITIONAL_VERBS = { "yield", "show" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		List<Integer> verbs = findVerbs(tokens);

		for (Integer verb : verbs) {

			for (int i = verb + 1; i <= verb + 4 && i < tokens.size(); i++) {
				Token nextToken = tokens.get(i);
				if (Arrays.stream(NEGATIVE_TERMS).anyMatch(t -> nextToken.getLemma().equals(t))) {
					return 1;
				}
			}

			if (verb - 1 >= 0) {
				Token prevToken = tokens.get(verb - 1);
				if (prevToken.getLemma().equals("nothing")) {
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
			if (token.getGeneralPos().equals("VB")
					|| Arrays.stream(ADDITIONAL_VERBS).anyMatch(t -> token.getLemma().equals(t))) {
				verbs.add(i);
			}
		}
		return verbs;
	}
}
