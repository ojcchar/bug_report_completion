package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ProblemInPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ErrorNounPhrasePM() };

	final public static Set<String> PREP_TERMS = JavaUtils.getSet("about", "as", "at", "between", "during", "for",
			"from", "in", "into", "of", "on", "over", "to", "up", "with", "within");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// System.out.println(sentence);

		for (Sentence ss : SentenceUtils.breakByParenthesis(sentence)) {
			List<Sentence> subSentences = SentenceUtils.findSubSentences(ss,
					SentenceUtils.findLemmasInTokens(JavaUtils.getSet("-", "--", ":"), ss.getTokens()));

			for (Sentence subSentence : subSentences) {
				List<Token> tokens = subSentence.getTokens();
				List<Integer> preps = findPrepositions(tokens);

				List<Integer> verbs = findProblematicVerbs(tokens);
				if (!verbs.isEmpty()) {
					continue;
				}

				List<Sentence> phrases = SentenceUtils.findSubSentences(subSentence, preps);
				for (Sentence phrase : phrases) {
					if (isNegative(phrase)) {
						return 1;
					}
				}
			}
		}

		return 0;

	}

	private List<Integer> findPrepositions(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(PREP_TERMS, tokens);
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
						if (!SentenceUtils.lemmasContainToken(PREP_TERMS, postToken)) {
							add = false;
						}

					}
				} else
				// disregard verbs that come before preposition
				if (i - 1 >= 0) {
					Token prevToken = tokens.get(i - 1);
					if (SentenceUtils.lemmasContainToken(PREP_TERMS, prevToken)) {
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
