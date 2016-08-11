package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorCondPM extends ObservedBehaviorPatternMatcher {

	public ErrorCondPM() {
	}

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens2 = sentence.getTokens();
		ArrayList<Integer> indexes = foundIndexToken(tokens2);
		if (indexes.size() > 0) {
			for (Integer integer : indexes) {
				if (integer > 0 && (integer < tokens2.size() - 1)) {
					List<Token> errorClause = tokens2.subList(0, integer);

					if (errorClause.get(0).getLemma().equals("no")
							|| (errorClause.get(0).getLemma().equals("nothing"))) {
						return 1;
					}

					for (Token errorToken : errorClause) {

						// error terms
						if (Arrays.stream(VerbErrorPM.ERROR_TERMS).anyMatch(t -> errorToken.getLemma().contains(t))
								&& errorToken.getGeneralPos().equals("NN")) {
							return 1;
						} else if ((Arrays.stream(NegativeAdjOrAdvPM.NEGATIVE_ADJ).anyMatch(
								t -> errorToken.getLemma().contains(t)) && errorToken.getGeneralPos().equals("JJ"))) {
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	private ArrayList<Integer> foundIndexToken(List<Token> tokens) {
		ArrayList<Integer> indexConditionalTerms = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(NegativeConditionalPM.TOKENS).anyMatch(t -> token.getLemma().contains(t))) {
				indexConditionalTerms.add(i);
			}
		}
		return indexConditionalTerms;
	}
}
