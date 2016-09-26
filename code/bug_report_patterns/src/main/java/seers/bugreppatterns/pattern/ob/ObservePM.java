package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ObservePM extends ObservedBehaviorPatternMatcher {

	private static String OBSERVE = "observe";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> observes = findObserve(tokens);

		if (!observes.isEmpty()) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findObserve(List<Token> tokens) {
		List<Integer> observe = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "observe"
			if (current.getGeneralPos().equals("VB") && current.getLemma().equals(OBSERVE)) {

				// The right "observe"
				if (current.getPos().equals("VBP") || current.getPos().equals("VB") || current.getPos().equals("VBD")
						|| current.getPos().equals("VBN")) {

					// the one in the first position
					if (i == 0) {
						observe.add(i);
					}
					// the one followed by ":" or "that"
					else if (i + 1 < tokens.size() && (tokens.get(i + 1).getLemma().equals(":")
							|| tokens.get(i + 1).getLemma().equals("that"))) {
						observe.add(i);
					}
					// the one preceded by an item marker, cardinal number, or
					// pronoun
					else if (i - 1 >= 0 && (tokens.get(i - 1).getGeneralPos().equals("LS")
							|| tokens.get(i - 1).getGeneralPos().equals("CD")
							|| tokens.get(i - 1).getGeneralPos().equals("PRP"))) {
						observe.add(i);
					}
				}
			}

		}
		return observe;
	}

}
