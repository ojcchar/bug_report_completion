package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ProblemIsPM extends ObservedBehaviorPatternMatcher {

	private final static Set<String> PROBLEM_TERMS = JavaUtils.getSet("problem", "issue");

	private final static Set<String> PREV_PROB_POS = JavaUtils.getSet("PRP", "DT", "CD", "JJ");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		if (tokens.size() < 1) {
			return 0;
		}

		int i = 0;
		Token current = tokens.get(i);

		while (PREV_PROB_POS.contains(current.getGeneralPos()) && i < tokens.size() - 1) {
			i++;
			current = tokens.get(i);
		}

		if (SentenceUtils.lemmasContainToken(PROBLEM_TERMS, current) && current.getGeneralPos().equals("NN")
				&& i < tokens.size() - 1) {
			i++;
			current = tokens.get(i);

			while (!current.getGeneralPos().equals("VB") && i < tokens.size() - 1) {
				i++;
				current = tokens.get(i);
			}

			if (current.getLemma().equals("be") && (current.getPos().equals("VBP") || current.getPos().equals("VBZ"))) {
				return 1;
			}
		}

		return 0;
	}

}
