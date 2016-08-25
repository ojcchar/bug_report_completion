package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BecomesAdjPM extends ObservedBehaviorPatternMatcher {

	private static String BECOME = "become";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> becomes = findBecomes(tokens);

		if (!becomes.isEmpty()) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findBecomes(List<Token> tokens) {
		List<Integer> becomes = new ArrayList<Integer>();

		for (int i = 1; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "become"
			if (current.getGeneralPos().equals("VB") && current.getLemma().equals(BECOME)) {
				if (i + 1 < tokens.size()) {
					Token next = tokens.get(i + 1);

					// The right "become": the one that comes before an adjective
					if (next.getGeneralPos().equals("JJ") || next.getPos().equals("VBN") || next.getPos().equals("RB")) {
						becomes.add(i);
					}
					
				}
			}

		}
		return becomes;
	}

}
