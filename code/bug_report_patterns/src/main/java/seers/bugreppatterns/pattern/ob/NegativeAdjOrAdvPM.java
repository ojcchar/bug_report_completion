package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeAdjOrAdvPM extends ObservedBehaviorPatternMatcher {

	final private static String[] NEGATIVE_WORDS = { "truncate" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> nots = findNots(tokens);

		for (Integer not : nots) {
			// Token auxToken = tokens.get(not - 1);
			Token nextToken = tokens.get(not + 1);

			if (nextToken.getGeneralPos().equals("RB")) {
				int index = not + 2;
				if (index < tokens.size()) {
					Token nextToken2 = tokens.get(index);
					if (nextToken2.getPos().equals("VBN")) {
						return 1;

					}
				}
			} else if (nextToken.getPos().equals("VBN")) {
				return 1;

			}
		}

		List<Integer> tobes = findToBeVerbs(tokens);

		for (Integer tobe : tobes) {
			Token nextToken = tokens.get(tobe + 1);

			if (nextToken.getPos().equals("VBN")) {
				if (Arrays.stream(NEGATIVE_WORDS).anyMatch(p -> nextToken.getLemma().equalsIgnoreCase(p))) {
					return 1;
				}
			}
		}

		return 0;
	}

	private List<Integer> findToBeVerbs(List<Token> tokens) {
		List<Integer> tobes = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			String pos = token.getGeneralPos();
			if (pos.equals("VB") && token.getLemma().equalsIgnoreCase("be")) {
				tobes.add(i);
			}
		}
		return tobes;
	}

	private List<Integer> findNots(List<Token> tokens) {

		List<Integer> nots = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			String pos = token.getPos();
			if ((pos.equals("RB") && token.getLemma().equalsIgnoreCase("not")) || token.getWord().equals("NOT")) {
				nots.add(i);
			}
		}
		return nots;
	}

}
