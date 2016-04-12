package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeAuxVerbPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		try {
			List<Token> tokens = sentence.getTokens();
			List<Integer> nots = findNots(tokens);

			for (Integer not : nots) {
				Token auxToken = tokens.get(not - 1);
				Token verbToken = tokens.get(not + 1);

				if (isAuxToken(auxToken)
				// && (verbToken.getGeneralPos().equals("VB")
				// || verbToken.getGeneralPos().equals("JJ") ||
				// verbToken.getGeneralPos().equals("RB"))
				) {
					return 1;
				} else {
					if (verbToken.getGeneralPos().equals("VB")) {
						return 1;
					} else if (verbToken.getGeneralPos().equals("NN")
							&& verbToken.getLemma().toLowerCase().endsWith("ing")) {
						return 1;
					}

				}

			}

			if (findAdditionalAuxVerbs(tokens)) {
				return 1;
			}
		} catch (IndexOutOfBoundsException e) {
		}

		return 0;
	}

	final private static String[] ADDITIONAL_AUX_VERBS = { "didnt", "doesn t", "doen t", "dosent", "haven t", "dont",
			"cant", "cannote", "don t", "s not", "can t", "wont", "isn t", "aren t", "ca not", "has no", "have no" };

	private boolean findAdditionalAuxVerbs(List<Token> tokens) {

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			String lemma = token.getLemma().toLowerCase();

			if (Arrays.stream(ADDITIONAL_AUX_VERBS).anyMatch(p -> lemma.equals(p))) {
				return true;
			}
		}

		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token1 = tokens.get(i);
			Token token2 = tokens.get(i + 1);
			String word = (token1.getLemma() + " " + token2.getLemma()).toLowerCase();

			if (Arrays.stream(ADDITIONAL_AUX_VERBS).anyMatch(p -> word.equals(p))) {
				return true;
			}
		}
		return false;
	}

	final private static String[] POS_LEMMAS = { "MD-can", "VB-do", "VB-be", "MD-would", "VB-have", "MD-will",
			"MD-could" };

	private boolean isAuxToken(Token auxToken) {
		String posLemma = auxToken.getGeneralPos() + "-" + auxToken.getLemma().toLowerCase();

		return Arrays.stream(POS_LEMMAS).anyMatch(p -> posLemma.equals(p));
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		return defaultMatchParagraph(paragraph);
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
