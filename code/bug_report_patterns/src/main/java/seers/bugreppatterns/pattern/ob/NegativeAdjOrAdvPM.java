package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeAdjOrAdvPM extends ObservedBehaviorPatternMatcher {

	final private static String[] NEGATIVE_VERBS = { "truncate", "outdate", "lose", "go", "strip", "break", "block",
			"add", "destroy", "reset", "mess" };

	final public static String[] NEGATIVE_ADJ = { "incorrect", "hard", "invalid", "inconsistent", "unclickable",
			"empty", "off", "undefined", "slow", "untrusted", "bad", "corrupt", "wrong", "strange", "blank",
			"undefined", "bloken", "infinite", "unstyled", "meaningless", "spurious", "inappropriate", "broken",
			"weird", "down", "dirty", "expensive", "inaccessible", "unexpected", "different", "duplicated", "unlimited",
			"inconsitant", "multiple", "unusable" };

	final public static String[] NEGATIVE_ADV = { "incorrectly", "randomly", "suddenly", "erroneously", "slow",
			"indefinitely", "twice" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		// cases such as: "is/are not" + (adverb) verb in pp or adj
		List<Integer> nots = findNots(tokens);
		for (Integer not : nots) {
			try {
				// Token auxToken = tokens.get(not - 1);
				Token nextToken = tokens.get(not + 1);

				if (nextToken.getGeneralPos().equals("RB")) {
					int index = not + 2;
					if (index < tokens.size()) {
						Token nextToken2 = tokens.get(index);
						if (nextToken2.getPos().equals("VBN") || nextToken2.getPos().equals("VBD")) {
							return 1;
						} else if (nextToken2.getPos().equals("JJ")) {
							return 1;
						}
					}
				} else if (nextToken.getPos().equals("VBN") || nextToken.getPos().equals("VB")
						|| nextToken.getPos().equals("VBD")) {
					return 1;

				} else if (nextToken.getPos().equals("JJ")) {
					return 1;
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}

		// cases such as is/are + negative verb in pp or adj
		List<Integer> tobes = findToBeVerbs(tokens);
		for (Integer tobe : tobes) {
			try {
				Token nextToken = tokens.get(tobe + 1);

				if (nextToken.getPos().equals("VBN") || nextToken.getPos().equals("VB")) {
					if (Arrays.stream(NEGATIVE_VERBS).anyMatch(p -> nextToken.getLemma().equalsIgnoreCase(p))) {
						return 1;
					}
				} else if (nextToken.getGeneralPos().equals("JJ")) {
					if (Arrays.stream(NEGATIVE_ADJ).anyMatch(p -> nextToken.getLemma().equalsIgnoreCase(p))) {
						return 1;
					}
				} else if (nextToken.getGeneralPos().equals("RB")) {
					int index = tobe + 2;
					if (index < tokens.size()) {
						Token nextToken2 = tokens.get(index);
						if (nextToken2.getPos().equals("VBN")) {
							if (Arrays.stream(NEGATIVE_VERBS)
									.anyMatch(p -> nextToken2.getLemma().equalsIgnoreCase(p))) {
								return 1;
							}
						} else if (nextToken2.getGeneralPos().equals("JJ")) {
							if (Arrays.stream(NEGATIVE_ADJ).anyMatch(p -> nextToken2.getLemma().equalsIgnoreCase(p))) {
								return 1;
							}
						}
					}
				} else if (nextToken.getGeneralPos().equals("IN")) {
					int index = tobe + 2;
					if (index < tokens.size()) {
						Token nextToken2 = tokens.get(index);
						if (nextToken.getLemma().equalsIgnoreCase("out")
								|| nextToken2.getLemma().equalsIgnoreCase("of")) {
							return 1;
						}
					}
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}

		boolean anyMatch2 = tokens.stream()
				.anyMatch(t -> t.getPos().equals("VBG") && t.getLemma().equalsIgnoreCase("miss"));
		if (anyMatch2) {
			return 1;
		}

		boolean anyMatch4 = tokens.stream()
				.anyMatch(t -> t.getPos().equals("VBN") && t.getLemma().equalsIgnoreCase("break"));
		if (anyMatch4) {
			return 1;
		}

		boolean anyMatch3 = tokens.stream()
				.anyMatch(t -> Arrays.stream(NEGATIVE_ADJ).anyMatch(p -> t.getLemma().equalsIgnoreCase(p)));
		if (anyMatch3) {
			return 1;
		}

		boolean anyMatch = tokens.stream()
				.anyMatch(t -> Arrays.stream(NEGATIVE_ADV).anyMatch(p -> t.getLemma().equalsIgnoreCase(p)));
		if (anyMatch) {
			return 1;
		}

		String str = TextProcessor.getStringFromLemmas(sentence);
		if (str.contains("go cpu bind")) {
			return 1;
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
