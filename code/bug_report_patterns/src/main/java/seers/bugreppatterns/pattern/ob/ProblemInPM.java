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

	final private static String[] OTHER_TERMS = { "about", "as", "at", "between", "during", "for", "from", "in", "into", "of",
			"on", "over", "to", "with", "within" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> preps = findPrepositions(tokens);
		
		int i = 0;
		int j = 0;
		while (j < preps.size()){

				Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(i, preps.get(j)));
				for (PatternMatcher pm : NEGATIVE_PMS) {
					int match = pm.matchSentence(sentence2);
					if (match == 1) {
						return 1;
					}
				}
			
			j++;
		}
//
//	List<Integer> errorTerms = findErrorTerms(tokens);for(
//	Integer errorTerm:errorTerms)
//
//	{
//
//		for (int i = errorTerm + 1; i <= errorTerm + 4 && i < tokens.size(); i++) {
//
//			Token nextToken = tokens.get(i);
//
//			if (Arrays.stream(OTHER_TERMS).anyMatch(t -> nextToken.getLemma().equals(t))) {
//				return 1;
//			}
//
//		}
//	}

	return 0;

	}

	private List<Integer> findErrorTerms(List<Token> tokens) {
		List<Integer> errorTerms = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(NegativeTerms.NOUNS).anyMatch(t -> token.getLemma().contains(t))) {
				errorTerms.add(i);
			}
		}
		return errorTerms;
	}

	private List<Integer> findPrepositions(List<Token> tokens) {
		List<Integer> prepTerms = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(OTHER_TERMS).anyMatch(t -> token.getLemma().contains(t))) {
				prepTerms.add(i);
			}
		}
		return prepTerms;
	}

}
