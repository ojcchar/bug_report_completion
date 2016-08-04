package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ConditionalNegativePM;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalAffirmativePM extends StepsToReproducePatternMatcher {

	ConditionalNegativePM pm = new ConditionalNegativePM();

	public final static String[] CONDITIONAL_TERMS = { "when", "if" };
	final static String[] EXCLUDED_VERBS = { "do", "be", "have", "want", "feel", "deal" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		if (tokens.stream().anyMatch(
				t -> t.getLemma().equals("will") || t.getLemma().equals("'ll") || t.getLemma().equals("ll"))) {
			return 0;
		}

		List<Integer> conditionalTerms = ConditionalNegativePM.findConditionalTerms(tokens, CONDITIONAL_TERMS);

		for (Integer condTerm : conditionalTerms) {

			if (condTerm + 1 < tokens.size()) {
				Token nextToken = tokens.get(condTerm + 1);
				if (nextToken.getPos().equals("VBG")
						&& Arrays.stream(EXCLUDED_VERBS).noneMatch(av -> nextToken.getLemma().equals(av))) {
					return 1;
				} else if (nextToken.getGeneralPos().equals("PRP") && !nextToken.getLemma().equals("it")) {
					if (condTerm + 2 < tokens.size()) {
						Token nextToken2 = tokens.get(condTerm + 2);
						if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ")
								|| nextToken2.getPos().equals("VB"))
								&& Arrays.stream(EXCLUDED_VERBS).noneMatch(av -> nextToken2.getLemma().equals(av))) {
							return 1;
						}
					}
				}

			}

		}

		return 0;
	}

}
