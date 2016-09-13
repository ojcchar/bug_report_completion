package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalNegativePM extends ObservedBehaviorPatternMatcher {

	private static final String[] PUNCTUATION = new String[] { ",", "_" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// split sentences based on "."
		List<Sentence> superSentences = findSubSentences(sentence, findPeriod(sentence.getTokens()));

		for (Sentence superSentence : superSentences) {
			List<Token> tokens = superSentence.getTokens();
			List<Integer> conditionalIndexes = findConditionals(tokens);

			if (!conditionalIndexes.isEmpty()) {

				// split sentences based on conditionals
				List<Sentence> subSentences = findSubSentences(superSentence, conditionalIndexes);

				// if there is a sentence before the conditional term, skip it -> the focus is on what is after the
				// conditional
				for (int i = conditionalIndexes.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {

					Sentence subSentece = subSentences.get(i);
					List<Integer> punct = findPunctuation(subSentece.getTokens());

					// hard case: there is no punctuation. Try subsentences from end to beginning. Check there is
					// something before the negative sentence.
					if (punct.isEmpty()) {
						boolean isNeg = false;
						for (int j = subSentece.getTokens().size() - 1; j > 0; j--) {
							Sentence negSent = new Sentence(subSentece.getId(),
									subSentece.getTokens().subList(j, subSentece.getTokens().size()));

							if (isNegative(negSent)) {
								isNeg = true;
								break;
							}

						}
						if (isNeg && subSentece.getTokens().size() > 1
								&& !findVerbs(subSentece.getTokens()).isEmpty()) {
							return 1;
						}

					}
					// The easy case: there is punctuation (',', '_', '-'). Make sure that (i) there is something
					// between the conditional and the punctuation, and (ii) there is a negative sentence after
					// the punctuation.
					else {
						List<Sentence> subSubSentences = findSubSentences(subSentece, punct);
						if (!subSubSentences.get(0).getTokens().isEmpty()) {
							for (int j = 1; j < subSubSentences.size(); j++) {

								if (isNegative(subSubSentences.get(j))) {
									return 1;
								}

							}
						}
					}

				}
			}

		}
		// If there's at least one conditional and none of the sub-sentences are negative
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}
	
	private List<Integer> findConditionals(List<Token> tokens) {
		return findLemmasInTokens(CONDITIONAL_TERMS, tokens);
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = findLemmasInTokens(PUNCTUATION, tokens);
		if (symbols.size() - 1 >= 0 && symbols.get(symbols.size() - 1) == tokens.size() - 1) {
			return symbols.subList(0, symbols.size() - 1);
		}
		return symbols;
	}

	private List<Integer> findPeriod(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		LinkedList<Character> pars = new LinkedList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals("-LRB-")) {
				pars.add('(');
			}
			if (token.getWord().equals("-RRB-")) {
				pars.removeLast();
			}
			if (token.getWord().equals(".") && pars.isEmpty()) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	private List<Integer> findVerbs(List<Token> tokens) {
		List<Integer> verbs = new ArrayList<>();
		boolean containsAux = false;
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			boolean add = true;
			if (token.getGeneralPos().equals("VB")) {
				if (i == 0) {
					add = false;
				} else if (token.getLemma().equals("be")
						|| token.getLemma().equals("have") && !token.getPos().equals("VB")) {
					containsAux = true;
				} else if ((token.getPos().equals("VBG") || token.getPos().equals("VBN")) && containsAux) {
					add = false;
				} else if (i - 1 >= 0 && tokens.get(i - 1).getWord().equalsIgnoreCase("to")
						&& token.getPos().equals("VB")) {
					add = false;
				}
				if (add) {
					verbs.add(i);
				}
			}
		}
		return verbs;
	}
}