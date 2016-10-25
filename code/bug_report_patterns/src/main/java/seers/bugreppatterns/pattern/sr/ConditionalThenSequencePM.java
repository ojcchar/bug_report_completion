package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalThenSequencePM extends StepsToReproducePatternMatcher {

	public final static Set<String> MODALS_AND_AUX = JavaUtils.getSet("will", "'ll", "ll", "could", "can", "may",
			"might");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (SentenceUtils.tokensContainAnyLemmaIn(tokens, MODALS_AND_AUX)) {
			return 0;
		}

		List<Integer> conditionalTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, tokens);

		for (Integer condTerm : conditionalTerms) {
			if (condTerm + 1 < tokens.size()) {
				Token nextToken = tokens.get(condTerm + 1);
				if (nextToken.getPos().equals("VBG")
						&& !SentenceUtils.lemmasContainToken(ConditionalObservedBehaviorPM.EXCLUDED_VERBS, nextToken)) {
					List<Token> subList = tokens.subList(condTerm + 1, tokens.size());
					if (checkForThenSequence(subList)) {
						return 1;
					}
				} else if (nextToken.getGeneralPos().equals("PRP") && !nextToken.getLemma().equals("it")) {
					if (condTerm + 2 < tokens.size()) {
						Token nextToken2 = tokens.get(condTerm + 2);
						if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ")
								|| nextToken2.getPos().equals("VB"))
								&& !SentenceUtils.lemmasContainToken(ConditionalObservedBehaviorPM.EXCLUDED_VERBS,
										nextToken2)) {
							List<Token> subList = tokens.subList(condTerm + 2, tokens.size());
							if (checkForThenSequence(subList)) {
								return 1;
							}
						}
					}
				}

			}
		}

		return 0;
	}

	private boolean checkForThenSequence(List<Token> tokens) {
		return tokens.stream()
				.filter(t -> t.getLemma().equals("then")).count() >1;
	}

}
