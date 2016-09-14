package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SameProblemWhenPM extends ObservedBehaviorPatternMatcher {

	static final public Set<String> OB_TERMS = JavaUtils.getSet("problem", "behavior", "result", "behaviour");

	static final public Set<String> SIMILARITY_TERMS = JavaUtils.getSet("same", "similar");

	static final public Set<String> LOC_AND_TEMP_PREP = JavaUtils.getSet("in", "on", "at", "since", "for", "before",
			"after", "from", "until", "till", "by", "beside", "under", "below", "over", "above", "across", "through",
			"into", "toward", "onto", "of", "with");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		boolean hasAVerb = hasAVerb(tokens);
		List<Integer> index_terms = findTerms(tokens);
		// boolean hasAClause = checkClause(tokens);

		for (Integer indexSim : findSimilarityTerms(tokens)) {

			// if (hasAVerb && hasAClause && !index_terms.isEmpty() && index_sim
			// >=0){
			if (hasAVerb && !index_terms.isEmpty() && indexSim >= 0) {
				// check the expression
				for (Integer index_term : index_terms) {
					if (index_term == (indexSim + 1)) {
						return 1;
					} else {
						Sentence beforeSimilarityTerms = new Sentence(sentence.getId(), tokens.subList(0, indexSim));
						List<Token> toks = beforeSimilarityTerms.getTokens();
						for (int i = 0; i < toks.size(); i++) {
							Token tok = toks.get(i);
							if (tok.getGeneralPos().equals("VB") && tok.getLemma().equals("be")
									&& ((indexSim == i + 1) || (indexSim == i + 2))) {
								return 1;
							}
						}
					}
				}

			}
		}

		return 0;
	}

	public List<Integer> findSimilarityTerms(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(SIMILARITY_TERMS, tokens);
	}

	public List<Integer> findTerms(List<Token> tokens) {
		List<Integer> termsList = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("NN") && SentenceUtils.lemmasContainToken(OB_TERMS, token)) {
				termsList.add(i);
			}
		}
		return termsList;
	}

	public boolean checkClause(List<Token> tokens) {

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (SentenceUtils.lemmasContainToken(CONDITIONAL_TERMS, token)
					|| SentenceUtils.lemmasContainToken(LOC_AND_TEMP_PREP, token)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasAVerb(List<Token> tokens) {

		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getGeneralPos().equals("VB")) {
				return true;
			}
		}
		return false;
	}
}
