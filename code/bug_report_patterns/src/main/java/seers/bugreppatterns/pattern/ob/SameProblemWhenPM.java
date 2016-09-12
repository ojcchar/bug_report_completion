package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SameProblemWhenPM extends ObservedBehaviorPatternMatcher {

	final public String[] terms = { "problem", "behavior", "result", "behaviour" };

	final public String[] similarityTerms = { "same", "similar" };

	final public String[] LOC_AND_TEMP_PREP = { "in", "on", "at", "since", "for", "before", "after", "from", "until",
			"till", "by", "beside", "under", "below", "over", "above", "across", "through", "into", "toward", "onto",
			"of", "with" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		boolean hasAVerb = hasAVerb(tokens);
		List<Integer> index_terms = findTerms(tokens);
		// boolean hasAClause = checkClause(tokens);
		int index_sim = findSimilarityTerms(tokens);

		// if (hasAVerb && hasAClause && !index_terms.isEmpty() && index_sim
		// >=0){
		if (hasAVerb && !index_terms.isEmpty() && index_sim >= 0) {
			// check the expression
			for (Integer index_term : index_terms) {
				if (index_term == (index_sim + 1)) {
					return 1;
				} else {
					Sentence beforeSimilarityTerms = new Sentence(sentence.getId(), tokens.subList(0, index_sim));
					List<Token> toks = beforeSimilarityTerms.getTokens();
					for (int i = 0; i < toks.size(); i++) {
						Token tok = toks.get(i);
						if (tok.getGeneralPos().equals("VB") && tok.getLemma().equals("be")
								&& ((index_sim == i + 1) || (index_sim == i + 2))) {
							return 1;
						}
					}
				}
			}

		}

		return 0;
	}

	public int findSimilarityTerms(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token before = tokens.get(i);
			if (Arrays.stream(similarityTerms).anyMatch(t -> before.getLemma().equals(t))) {
				return i;
			}
		}
		return -1;
	}

	public List<Integer> findTerms(List<Token> tokens) {
		List<Integer> termsList = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("NN")
					&& (Arrays.stream(terms).anyMatch(t -> token.getLemma().equals(t)))) {
				termsList.add(i);
			}
		}
		return termsList;
	}

	public boolean checkClause(List<Token> tokens) {

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(CONDITIONAL_TERMS).anyMatch(t -> token.getLemma().equals(t))
					|| (Arrays.stream(LOC_AND_TEMP_PREP).anyMatch(t -> token.getLemma().equals(t)))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * public int findIndexEpression(List<Token> tokens) {
	 * 
	 * for (int i = 1; i < tokens.size(); i++) { Token token = tokens.get(i);
	 * Token previous = tokens.get(i - 1); if
	 * ((token.getGeneralPos().equals("NN")) && (Arrays.stream(terms).anyMatch(t
	 * -> token.getLemma() .equals(t))) &&
	 * (Arrays.stream(similarityTerms).anyMatch(t -> previous
	 * .getLemma().equals(t)))) { return i;
	 * 
	 * } } return -1; }
	 */

	public boolean hasAVerb(List<Token> tokens) {

		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getGeneralPos().equals("VB")) {
				return true;
			}
		}
		return false;
	}
}
