package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AssumePM extends ObservedBehaviorPatternMatcher {

	private static String ASSUME = "assume";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> assumes = findAssume(tokens);

		if (!assumes.isEmpty()) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findAssume(List<Token> tokens) {
		List<Integer> assumes = new ArrayList<Integer>();

		for (int i = 1; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "assume"
			if (current.getGeneralPos().equals("VB") && current.getLemma().equals(ASSUME)) {
				Token previous = tokens.get(i - 1);

				// The right "assume": the one that comes after a noun
				if (previous.getGeneralPos().equals("NN")) {
					assumes.add(i);
				}
				// Sometimes there are parenthesis in between. We skip their content and look for a noun before the
				// parenthesis
				else if (previous.getGeneralPos().equals("-RRB-")) {
					int j = i - 2;
					while (j >= 0) {
						if (tokens.get(j).getGeneralPos().equals("-LRB-")) {
							break;
						}
						j--;
					}

					if (j > 0) {
						if (tokens.get(j - 1).getGeneralPos().equals("NN")) {
							assumes.add(i);
						}
					}
				} 
				// the auxiliar case
				else if (previous.getGeneralPos().equals("VB")) {
					int j = i - 2;
					while (j >= 0 && tokens.get(j).getGeneralPos().equals("VB")) {
						j--;
					}

					if (j >= 0) {
						if (tokens.get(j).getGeneralPos().equals("NN")) {
							assumes.add(i);
						}
					}
				}

			}

		}
		return assumes;
	}

}
