package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ProblemInPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ErrorNounPhrasePM() };

	final private static String[] OTHER_TERMS = { "about", "as", "at", "between", "during", "for", "from", "in", "into",
			"of", "on", "over", "to", "with", "within" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> preps = findPrepositions(tokens);

		int i = 0;
		int j = 0;
		
		boolean works = false;
		while (j < preps.size()) {

			Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(i, preps.get(j)));
			for (PatternMatcher pm : NEGATIVE_PMS) {
				int match = pm.matchSentence(sentence2);
				if (match == 1) {
					works = works || true;
				}
			}

			j++;
		}
		if (works) {
			return 1;
		}
		return 0;

	}

	public static List<Integer> findPrepositions(List<Token> tokens) {
		List<Integer> prepTerms = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(OTHER_TERMS).anyMatch(t -> token.getWord().equals(t))) {
				prepTerms.add(i);
			}
		}
		return prepTerms;
	}

}
