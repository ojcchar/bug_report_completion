package seers.bugreppatterns.pattern.eb;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/*
 * This one applies only for titles
 */
public class ImperativeSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		int match = 0;
		try {
			List<Token> tokens = sentence.getTokens();
			Token firstToken = tokens.get(0);
			Token secondToken = tokens.get(1);

			match = checkNormalCase(firstToken, secondToken);

			if (match == 0) {

				String text = TextProcessor.getStringFromLemmas(sentence);

				// ----------------

				Token fourthToken = tokens.get(3);
				Token firfthToken = tokens.get(4);

				// matches label
				boolean b = text.matches("expect (result|behavior) :.+");
				if (b) {
					match = checkNormalCase(fourthToken, firfthToken);
				} else {

					Token thirdToken = tokens.get(2);
					if (firstToken.getPos().equals("-LRB-") && thirdToken.getPos().equals("-RRB-")) {

						match = checkNormalCase(fourthToken, firfthToken);

					} else {
						match = checkNormalCaseWithLabel(tokens, ":");

					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}

		return match;

	}

	public static int checkNormalCaseWithLabel(List<Token> tokens, String separator) {
		int idx = -1;
		for (int i = 0; i < tokens.size() && i <= 5; i++) {

			Token token = tokens.get(i);
			if (token.getLemma().equals(separator)) {
				idx = i;
			}
		}

		if (idx != -1) {
			return checkNormalCase(tokens.get(idx + 1), tokens.get(idx + 2));
		}
		return 0;
	}

	final private static String[] UNDETECTED_VERBS = { "show", "boomark", "rename", "run", "select", "post", "load",
			"support", "change" };

	public static int checkNormalCase(Token firstToken, Token secondToken) {

		if (firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP")) {
			return 1;
		} else {
			if (secondToken != null) {
				if (firstToken.getPos().equals("RB")
						&& (secondToken.getPos().equals("VB") || secondToken.getPos().equals("VBP"))) {
					return 1;
				}
			}
			if (Arrays.stream(UNDETECTED_VERBS).anyMatch(p -> firstToken.getLemma().equals(p))) {
				return 1;
			}
		}
		return 0;
	}

}
