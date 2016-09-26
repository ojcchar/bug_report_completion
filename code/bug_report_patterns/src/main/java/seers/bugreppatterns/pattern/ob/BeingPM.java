package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BeingPM extends ObservedBehaviorPatternMatcher {

	private static String BE = "be";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> beings = findBeing(tokens);

		if (!beings.isEmpty() && beings.get(0) > 0) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findBeing(List<Token> tokens) {
		List<Integer> beings = new ArrayList<Integer>();

		for (int i = 1; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "being"
			if (current.getPos().equals("VBG") && current.getLemma().equals(BE)) {
				if (i + 1 < tokens.size()) {
					Token next = tokens.get(i + 1);

					// The right "being": the one that comes before a past
					// participle verb
					if (next.getPos().equals("VBN")) {
						beings.add(i);
					}

				}
			}

		}
		return beings;
	}

}
