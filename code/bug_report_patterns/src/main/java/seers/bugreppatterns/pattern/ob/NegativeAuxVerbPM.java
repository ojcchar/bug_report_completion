package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeAuxVerbPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> nots = findNots(tokens);

		for (int i = 0; i < nots.size(); i++) {
			Integer not = nots.get(i);

			// no previous token
			if (not - 1 < 0) {
				continue;
			}

			Token previousToken = tokens.get(not - 1);
			// regular case: "the user is not..."
			if (isAuxiliaryToken(previousToken)) {
				return 1;
			}
			// case: "the user is maybe not..."
			else if (not - 2 >= 0 && previousToken.getGeneralPos().equals("RB")
					&& isAuxiliaryToken(tokens.get(not - 2))) {
				return 1;

			} else {
				// case: the user is ... and (hence also) not ...
				// the "hence also" is optional
				// basically we accept a lot of RBs preceding the not and then
				// an
				// "and"
				{
					// the following condition checks for no previous "not"s
					if (i == 0) {

						// find the "and" preceding the RBs (if any)
						int andIdx = -1;
						boolean allAreTokensRb = true;
						for (int j = not - 1; j >= 0; j--) {
							Token currentToken = tokens.get(j);
							if (allAreTokensRb && currentToken.getGeneralPos().equals("CC")
									&& currentToken.getLemma().equals("and")) {
								andIdx = j;
								break;
							} else if (!currentToken.getGeneralPos().equals("RB")) {
								allAreTokensRb = false;
								break;
							}

						}

						if (andIdx != -1) {
							List<Token> subStncTokens = tokens.subList(0, andIdx - 1);
							// any auxiliary verb?
							boolean thereIsAuxToken = subStncTokens.stream().anyMatch(tok -> isAuxiliaryToken(tok));
							if (thereIsAuxToken) {
								return 1;
							}
						}
					}
				}

				// case: "the user is making [subject] not... "
				if (not - 3 >= 0) {
					Token previousToken2 = tokens.get(not - 2);
					Token previousToken3 = tokens.get(not - 3);
					if (isAuxiliaryToken(previousToken3) && previousToken2.getPos().equals("VBG")
							&& previousToken2.equals("make") && previousToken.getGeneralPos().equals("NN")
							|| previousToken.getGeneralPos().equals("PRP")) {
						return 1;
					}

				}

			}

		}

		// find misspelled cases
		if (findAdditionalAuxVerbs(tokens)) {
			return 1;
		}

		return 0;
	}

	final private static String[] ADDITIONAL_AUX_VERBS = { "didnt", "doesn t", "doen t", "dosent", "haven t", "dont",
			"cant", "cannote", "don t", "s not", "can t", "wont", "isn t", "isnt", "aren t", "ca not", "has no",
			"have no", "didn t", "dose not" };

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

	final private static Set<String> POS_LEMMAS = JavaUtils.getSet("MD-can", "VB-do", "VB-be", "MD-would", "VB-have",
			"MD-will", "MD-could", "MD-may");

	private boolean isAuxiliaryToken(Token auxToken) {
		String posLemma = auxToken.getGeneralPos() + "-" + auxToken.getLemma().toLowerCase();
		return POS_LEMMAS.stream().anyMatch(p -> posLemma.equals(p));
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
