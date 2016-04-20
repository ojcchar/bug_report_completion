package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WouldBeSentencePM extends ExpectedBehaviorPatternMatcher {

	final private static String[] MODALS = { "would", "might" };
	final private static String[] ADJECTIVES = { "nice", "great", "super", "useful", "convenient", "ideal", "neat",
			"better", "helpful", "fine" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		List<Integer> pronounTokens = findPronounTokens(tokens);

		for (Integer pronoun : pronounTokens) {

			try {
				Token modal = tokens.get(pronoun + 1);
				if (modal.getPos().equals("MD")
						&& Arrays.stream(MODALS).anyMatch(m -> modal.getLemma().equalsIgnoreCase(m))) {

					Token advOrVerbToBe1 = tokens.get(pronoun + 2);

					if (advOrVerbToBe1.getLemma().equalsIgnoreCase("be")) {
						Token adjOrAdj = tokens.get(pronoun + 3);
						if (adjOrAdj.getGeneralPos().equals("RB")) {
							Token adj = tokens.get(pronoun + 4);
							if (adj.getPos().equals("JJ")
									&& Arrays.stream(ADJECTIVES).anyMatch(m -> adj.getLemma().equalsIgnoreCase(m))) {
								return 1;
							}
						} else if (adjOrAdj.getPos().equals("JJ")
								&& Arrays.stream(ADJECTIVES).anyMatch(m -> adjOrAdj.getLemma().equalsIgnoreCase(m))) {
							return 1;
						}

					} else if (advOrVerbToBe1.getPos().equals("RB")) {
						Token advOrVerbToBe2 = tokens.get(pronoun + 3);
						if (advOrVerbToBe2.getLemma().equalsIgnoreCase("be")) {
							Token adjOrAdj = tokens.get(pronoun + 4);
							if (adjOrAdj.getPos().equals("RB")) {
								Token adj = tokens.get(pronoun + 5);
								if (adj.getPos().equals("JJ") && Arrays.stream(ADJECTIVES)
										.anyMatch(m -> adj.getLemma().equalsIgnoreCase(m))) {
									return 1;
								}
							} else if (adjOrAdj.getPos().equals("JJ") && Arrays.stream(ADJECTIVES)
									.anyMatch(m -> adjOrAdj.getLemma().equalsIgnoreCase(m))) {
								return 1;
							}

						}
					}
				}
			} catch (IndexOutOfBoundsException e) {
			}

		}

		return 0;
	}

	private List<Integer> findPronounTokens(List<Token> tokens) {

		List<Integer> pronounTokens = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			String pos = token.getPos();
			if (pos.equals("PRP") || pos.equals("DT")) {
				pronounTokens.add(i);
			}
		}
		return pronounTokens;
	}

}
