package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AllowPM extends ObservedBehaviorPatternMatcher {

	private static String ALLOW = "allow";

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM()};
	
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Sentence> subSentences = findSubSentences(sentence, findPunctuation(sentence.getTokens()));

		for (Sentence subSentence : subSentences) {
			List<Integer> allows = findAllows(subSentence.getTokens());

			if (!allows.isEmpty() && !isEBModal(subSentence.getTokens()) && !isNegative(subSentence)) {
				return 1;
			}

		}
		return 0;
	}

	private List<Integer> findAllows(List<Token> tokens) {
		List<Integer> assumes = new ArrayList<Integer>();

		for (int i = 1; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// Look for every "allow"
			if ((current.getPos().equals("VBZ") || current.getPos().equals("VBP") || current.getPos().equals("VB"))
					&& current.getLemma().equals(ALLOW)) {
				Token previous = tokens.get(i - 1);

				// The right "allow": the one that comes after a noun, pronoun or wh-determiner
				if (isRightSubject(previous)) {
					assumes.add(i);
				}
				// Sometimes there are parenthesis in between. We skip their content and look for a
				// noun/pronoun/wh-determiner before the parenthesis
				else if (previous.getGeneralPos().equals("-RRB-")) {
					int j = i - 2;
					while (j >= 0) {
						if (tokens.get(j).getGeneralPos().equals("-LRB-")) {
							break;
						}
						j--;
					}

					if (j > 0) {
						if (isRightSubject(tokens.get(j - 1))) {
							assumes.add(i);
						}
					}
				}
				// the auxiliar case
				else if (previous.getGeneralPos().equals("VB")) {
					int j = i - 2;
					while (j >= 0 && tokens.get(j).getGeneralPos().equals("VB")) {
						j--;
					}
					// verify that there is a noun/pronoun/wh-determiner before the verb(s)
					if (j >= 0) {
						if (isRightSubject(tokens.get(j))) {
							assumes.add(i);
						}
					}
				}

			}

		}
		return assumes;
	}

	private boolean isRightSubject(Token token) {
		return token.getGeneralPos().equals("NN") || token.getGeneralPos().equals("PRP")
				|| token.getPos().equals("WDT");
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals(";") || token.getWord().equals(",") || token.getWord().equals("_")
					|| token.getWord().equals("-") || token.getWord().equals("-LRB-") || token.getWord().equals("-RRB-")
					|| token.getWord().equals(".")) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		for (PatternMatcher pm : NEGATIVE_PMS) {
			int match = pm.matchSentence(sentence);
			if (match == 1) {
				return true;
			}
		}
		return false;
	}
}
