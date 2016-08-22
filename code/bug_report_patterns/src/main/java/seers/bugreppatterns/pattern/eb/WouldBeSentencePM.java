package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WouldBeSentencePM extends ExpectedBehaviorPatternMatcher {

	final private static String[] MODALS = { "would", "might" };

	final private static String[] POSITIVE_ADJECTIVES = { "nice", "great", "super", "useful", "convenient", "ideal",
			"neat", "better", "helpful", "fine", "cool", "good", "optimal", "fantastic" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		List<Integer> modalTokens = findModalTokens(tokens);

		for (Integer modal : modalTokens) {

			try {
				Token advOrVerbToBe1 = tokens.get(modal + 1);

				if (advOrVerbToBe1.getLemma().equals("be")) {

					Token adjOrAdj = tokens.get(modal + 2);

					if (adjOrAdj.getGeneralPos().equals("RB")) {

						Token adj = tokens.get(modal + 3);

						if (adj.getPos().equals("JJ") && Arrays.stream(POSITIVE_ADJECTIVES)
								.anyMatch(m -> adj.getLemma().equalsIgnoreCase(m))) {
							return 1;
						}
					} else if (adjOrAdj.getPos().equals("JJ") && Arrays.stream(POSITIVE_ADJECTIVES)
							.anyMatch(m -> adjOrAdj.getLemma().equalsIgnoreCase(m))) {
						return 1;
					}
				} else if (advOrVerbToBe1.getPos().equals("RB")) {
					Token advOrVerbToBe2 = tokens.get(modal + 2);
					if (advOrVerbToBe2.getLemma().equals("be")) {
						Token adjOrAdj = tokens.get(modal + 3);
						if (adjOrAdj.getPos().equals("RB")) {
							Token adj = tokens.get(modal + 4);
							if (adj.getPos().equals("JJ") && Arrays.stream(POSITIVE_ADJECTIVES)
									.anyMatch(m -> adj.getLemma().equalsIgnoreCase(m))) {
								return 1;
							}
						} else if (adjOrAdj.getPos().equals("JJ") && Arrays.stream(POSITIVE_ADJECTIVES)
								.anyMatch(m -> adjOrAdj.getLemma().equalsIgnoreCase(m))) {
							return 1;
						}

					}
				}
			} catch (IndexOutOfBoundsException e) {
			}

		}

		return 0;
	}

	private List<Integer> findModalTokens(List<Token> tokens) {

		List<Integer> modalTokens = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token modal = tokens.get(i);
			if ((modal.getPos().equals("MD")
					&& Arrays.stream(MODALS).anyMatch(m -> modal.getLemma().equalsIgnoreCase(m)))
					|| modal.getLemma().equals("d")) {
				modalTokens.add(i);
			}
		}
		return modalTokens;
	}

}
