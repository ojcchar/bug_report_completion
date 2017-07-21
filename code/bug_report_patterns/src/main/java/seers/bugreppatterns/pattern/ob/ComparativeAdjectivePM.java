package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ComparativeAdjectivePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> comparatives = findComparatives(tokens);

		if (!comparatives.isEmpty() && !isEBModal(tokens)) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findComparatives(List<Token> tokens) {
		List<Integer> comparatives = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			if ((current.getPos().equals("JJR") || current.getPos().equals("RBR"))
					&& !(current.getLemma().equals(">") || current.getLemma().equals("<"))) {
				comparatives.add(i);
			}

		}
		return comparatives;
	}
}
