package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VeryAdjectivePM extends ObservedBehaviorPatternMatcher {

	public final static String VERY = "very";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		// split sentences based on punctuation
		List<Sentence> subSentences = findSubSentences(sentence, findPunctuation(tokens));

		for (Sentence subSentence : subSentences) {
			List<Integer> veryTerms = findVeryAdj(subSentence.getTokens());
	
			if (!veryTerms.isEmpty() && !isEBModal(subSentence.getTokens())) {
				return 1;
			}
		}

		return 0;
	}

	private List<Integer> findVeryAdj(List<Token> tokens) {
		List<Integer> veryTerms = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "very"
			if (current.getGeneralPos().equals("RB") && current.getLemma().equals(VERY)) {

				// the right "very"
				if (i + 1 < tokens.size()) {
					Token next = tokens.get(i + 1);
					// The one that precedes an adjective or an adverb
					if (next.getGeneralPos().equals("JJ") || next.getGeneralPos().equals("RB")) {
						veryTerms.add(i);
					}
				}

			}

		}
		return veryTerms;
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals(",") || token.getWord().equals("_") || token.getWord().equals(".")
					|| token.getWord().equals(";")) {
				symbols.add(i);
			}
		}
		return symbols;
	}

}
