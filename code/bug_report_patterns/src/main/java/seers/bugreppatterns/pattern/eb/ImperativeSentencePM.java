package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.entity.Paragraph;
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
			Token firstToken = sentence.getTokens().get(0);
			Token secondToken = sentence.getTokens().get(1);

			match = checkNormalCase(firstToken, secondToken);

			if (match == 0) {

				String text = TextProcessor.getStringFromSentence(sentence);

				// ----------------

				Token fourthToken = sentence.getTokens().get(3);
				Token firfthToken = sentence.getTokens().get(4);

				boolean b = text.matches("expect (result|behavior) :.+");
				if (b) {

					match = checkNormalCase(fourthToken, firfthToken);

				} else {

					Token thirdToken = sentence.getTokens().get(2);
					if (firstToken.getPos().equals("-LRB-") && thirdToken.getPos().equals("-RRB-")) {

						match = checkNormalCase(fourthToken, firfthToken);

					} else {
						if (secondToken.getPos().equals(":")) {

							match = checkNormalCase(thirdToken, fourthToken);

						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}

		return match;

	}

	private int checkNormalCase(Token firstToken, Token secondToken) {

		if (firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP")) {
			return 1;
		} else {
			if (firstToken.getPos().equals("RB")
					&& (secondToken.getPos().equals("VB") || secondToken.getPos().equals("VBP"))) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		return defaultMatchParagraph(paragraph);
	}

}
