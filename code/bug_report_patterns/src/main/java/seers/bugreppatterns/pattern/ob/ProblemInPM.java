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
		List<Integer> verbs = findVerbs(tokens);
		int i = 0;
		int j = 0;
		
		boolean works = false;
		while (j < preps.size()) {

			Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(i, preps.get(j)));
			for (PatternMatcher pm : NEGATIVE_PMS) {
				int match = pm.matchSentence(sentence2);
				if (match == 1) {
					if (verbs.isEmpty()) {
						works = works || true;
					}
//					for (int verb = 0; verb < verbs.size(); verb++) {
//						if (preps.get(j) < verbs.get(verb)) {
//							return 0;
//						} else {
//							works = works || true;
//						}
//					}

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

	private List<Integer> findVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				boolean add = true;
				// disregard verb if it starts the sentence
				if (i == 0) {
					add = false;
				} else
					// disregard gerund and past participle verbs 
					if (token.getPos().equals("VBG")
						|| token.getPos().equals("VBD") || token.getPos().equals("VBN")) {
					add = false;
				} else
				// disregard verbs that come after preposition
				if (i - 1 >= 0) {
					Token prevToken = tokens.get(i - 1);
					if (Arrays.stream(OTHER_TERMS).anyMatch(t -> prevToken.getWord().equalsIgnoreCase(t))) {
						add = false;
					}

				}
				if (add)
					verbs.add(i);
			}
		}
		return verbs;
	}

}
