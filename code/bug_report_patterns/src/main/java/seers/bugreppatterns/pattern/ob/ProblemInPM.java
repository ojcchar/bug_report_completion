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

	final public static String[] PREP_TERMS = { "about", "as", "at", "between", "during", "for", "from", "in", "into",
			"of", "on", "over", "to", "up", "with", "within" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// System.out.println(sentence);
		List<Token> tokens = sentence.getTokens();
		List<Integer> preps = findPrepositions(tokens);

		List<Integer> verbs = findProblematicVerbs(tokens);
		if (!verbs.isEmpty()) {
			return 0;
		}

		int i = 0;
		int j = 0;

		while (j < preps.size()) {

			Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(i, preps.get(j)));
			if(isNegative(sentence2)) {
				return 1;
			}

			j++;
		}

		return 0;

	}

	private List<Integer> findPrepositions(List<Token> tokens) {
		return findLemmasInTokens(PREP_TERMS, tokens);
	}

	private List<Integer> findProblematicVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			if (token.getGeneralPos().equals("VB")) {
				boolean add = true;
				// disregard verb if it starts the sentence
				if (i == 0) {
					add = false;
				} else
				// cases with gerund and past participle verbs
				if (token.getPos().equals("VBG") || token.getPos().equals("VBD") || token.getPos().equals("VBN")) {
					if (i - 1 >= 0) {
						Token prevToken = tokens.get(i - 1);
						// disregard verbs that come after a noun 
						if (!prevToken.getGeneralPos().equals("NN")) {
							add = false;
						}
					}
					if (i + 1 < tokens.size()) {
						Token postToken = tokens.get(i + 1);
						// disregard verbs that go after a preposition
						if (!Arrays.stream(PREP_TERMS).anyMatch(t -> postToken.getWord().equalsIgnoreCase(t))) {
								add = false;
						}

					}
				} else
				// disregard verbs that come before preposition
				if (i - 1 >= 0) {
					Token prevToken = tokens.get(i - 1);
					if (Arrays.stream(PREP_TERMS).anyMatch(t -> prevToken.getWord().equalsIgnoreCase(t))) {
						add = false;
					}

				}
				if (add)
					verbs.add(i);
			}
		}
		return verbs;
	}
	
	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

}
