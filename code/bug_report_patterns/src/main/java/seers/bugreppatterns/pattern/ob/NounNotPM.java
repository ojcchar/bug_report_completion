package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NounNotPM extends ObservedBehaviorPatternMatcher {

	public NounNotPM() {
	}

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens2 = sentence.getTokens();
		ArrayList<Integer> notTokens = foundIndexToken(tokens2);
		for (Integer notTok : notTokens) {
			if (notTok - 1 >= 0) {

				// noun before 'not'
				Token nounToken = getNounToken(tokens2, notTok);
				if (nounToken == null) {
					continue;
				}

				if (notTok + 1 < tokens2.size()) {
					int nextIdx = getNextValidToken(tokens2, notTok + 1);

					if (nextIdx != -1) {

						Token token = tokens2.get(nextIdx);

						// after the 'not'
						if (token.getGeneralPos().equals("JJ") || token.getPos().equals("VBG")
								|| token.getPos().equals("VBN") || token.getPos().equals("VBD")
								|| token.getPos().equals("VB")) {
							return 1;
						} else {

							int nextNextIndx = getNextValidToken(tokens2, nextIdx + 1);
							// adverb next to the 'not'
							if (nextNextIndx != -1) {
								Token token2 = tokens2.get(nextNextIndx);
								if (token.getGeneralPos().equals("RB")) {

									if (token2.getGeneralPos().equals("JJ") || token2.getPos().equals("VBG")
											|| token2.getPos().equals("VBN") || token2.getPos().equals("VBD")
											|| token2.getPos().equals("VB")) {
										return 1;
									}
								} else if (token.getPos().equals("VB")) {

									if (token2.getGeneralPos().equals("RB")) {
										return 1;
									}
								}

							}
						}
					}
				}

			}
		}
		return 0;
	}

	private int getNextValidToken(List<Token> tokens2, int ini) {

		for (int i = ini; i < tokens2.size(); i++) {
			if (tokens2.get(i).getLemma().matches("[a-zA-Z]+")) {
				return i;
			}
		}
		return -1;
	}

	private Token getNounToken(List<Token> tokens2, Integer notTok) {

		for (int i = notTok - 1; i >= 0; i--) {
			Token token = tokens2.get(i);
			if (token.getGeneralPos().equals("NN")) {
				return token;
			} else if (token.getLemma().matches("[a-zA-Z]+")) {
				return null;
			}
		}

		return null;
	}

	private ArrayList<Integer> foundIndexToken(List<Token> tokens) {
		ArrayList<Integer> indexConditionalTerms = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			if (token.getLemma().equals("not")) {
				indexConditionalTerms.add(i);
			}
		}
		return indexConditionalTerms;
	}

}
