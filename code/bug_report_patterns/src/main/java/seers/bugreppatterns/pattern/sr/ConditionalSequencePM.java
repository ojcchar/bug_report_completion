package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalSequencePM extends StepsToReproducePatternMatcher {
	public final static String[] COND_TERMS = { "when", "if" };
	public final static String[] MODALS_AND_AUX = { "will", "'ll", "ll", "could", "can", "may", "might" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (tokens.stream().anyMatch(t -> Arrays.stream(MODALS_AND_AUX).anyMatch(m -> t.getLemma().equals(m)))) {
			return 0;
		}

		List<Integer> conditionalTerms = findTermsInTokens(COND_TERMS, tokens);

		for (Integer condTerm : conditionalTerms) {
			if (condTerm + 1 < tokens.size()) {
				Token nextToken = tokens.get(condTerm + 1);
				if (nextToken.getPos().equals("VBG") && Arrays.stream(ConditionalAffirmativePM.EXCLUDED_VERBS)
						.noneMatch(av -> nextToken.getLemma().equals(av))) {
					if (tokens.subList(condTerm + 1, tokens.size()).stream()
							.anyMatch(t -> t.getLemma().equals("then"))) {
						return 1;
					}
				} else if (nextToken.getGeneralPos().equals("PRP") && !nextToken.getLemma().equals("it")) {
					if (condTerm + 2 < tokens.size()) {
						Token nextToken2 = tokens.get(condTerm + 2);
						if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ")
								|| nextToken2.getPos().equals("VB"))
								&& Arrays.stream(ConditionalAffirmativePM.EXCLUDED_VERBS)
										.noneMatch(av -> nextToken2.getLemma().equals(av))) {
							if (tokens.subList(condTerm + 2, tokens.size()).stream()
									.anyMatch(t -> t.getLemma().equals("then"))) {
								return 1;
							}
						}
					}
				} else {
					List<Integer> tobes = findToBeVerbs(tokens, condTerm + 1);
					for (Integer tobe : tobes) {
						if (tobe + 1 < tokens.size()) {
							Token nexTok = tokens.get(tobe + 1);

							if (nexTok.getPos().equals("VBN") || nexTok.getPos().equals("VB")
									|| nexTok.getGeneralPos().equals("JJ")) {
								if (tokens.subList(tobe + 1, tokens.size()).stream()
										.anyMatch(t -> t.getLemma().equals("then"))) {
									return 1;
								}
							}
						}
					}
				}

			}
		}

		return 0;
	}

	private List<Integer> findToBeVerbs(List<Token> tokens, int j) {
		List<Integer> tobes = new ArrayList<>();
		for (int i = j; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			String pos = token.getGeneralPos();
			if (pos.equals("VB") && token.getLemma().equalsIgnoreCase("be")) {
				tobes.add(i);
			}
		}
		return tobes;
	}

}
