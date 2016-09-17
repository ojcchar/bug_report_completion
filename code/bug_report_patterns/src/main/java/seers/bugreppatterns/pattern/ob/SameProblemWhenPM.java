package seers.bugreppatterns.pattern.ob;

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

	@SuppressWarnings("unused")
	static final private Set<String> LOC_AND_TEMP_PREP = JavaUtils.getSet("in", "on", "at", "since", "for", "before",
			"after", "from", "until", "till", "by", "beside", "under", "below", "over", "above", "across", "through",
			"into", "toward", "onto", "of", "with");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		if (isEBModal(tokens)) {
			return 0;
		}
		List<Integer> simTerms = findSimilarityTerms(tokens);
		for (Integer simTerm : simTerms) {
			if (simTerm + 1 < tokens.size() && SentenceUtils.lemmasContainToken(OB_TERMS, tokens.get(simTerm + 1))) {
				return 1;
			} else if (simTerm - 1 >= 0) {
				int previous = simTerm - 1;
				while (previous >= 0 && tokens.get(previous).getGeneralPos().equals("DT")) {
					previous--;
				}

				if (previous >= 0 && tokens.get(previous).getLemma().equals("be")) {
					return 1;
				}

			}
		}

		return 0;
	}

	private List<Integer> findSimilarityTerms(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(SIMILARITY_TERMS, tokens);
	}
}
