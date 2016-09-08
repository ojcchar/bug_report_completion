package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class DueToNegativePM extends ObservedBehaviorPatternMatcher {

	private static String DUE = "due";
	private static String TO = "to";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> dueTos = findDueTos(tokens);

		if (!dueTos.isEmpty()) {
			for (int i = 0; i < dueTos.size(); i++) {
				
				// The sentence after due to must be negative
				int start = dueTos.get(i) + 2;
				int end = i == dueTos.size() - 1 ? tokens.size() : dueTos.get(i + 1);
				Sentence subSentence = new Sentence(sentence.getId(), tokens.subList(start, end));
			
				if (isNegative(subSentence)) {
					return 1;
				}
			}
			
		}

		return 0;
	}

	private List<Integer> findDueTos(List<Token> tokens) {
		List<Integer> dueTos = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "due"
			if (current.getPos().equals("JJ") && current.getLemma().equals(DUE)) {
				if (i + 1 < tokens.size()) {
					Token next = tokens.get(i + 1);

					// The right "due": the one that is followed by "to"
					if (next.getPos().equals("TO") && next.getLemma().equals(TO)) {
						dueTos.add(i);
					}

				}
			}

		}
		return dueTos;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NegativeAfterPM.NEGATIVE_PMS);
	}
}
