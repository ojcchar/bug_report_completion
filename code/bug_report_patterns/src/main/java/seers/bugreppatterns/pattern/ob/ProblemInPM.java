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
			List<Sentence> subSentences = SentenceUtils.extractClauses(ss);
			// SentenceUtils.findSubSentences(ss,
			// SentenceUtils.findLemmasInTokens(JavaUtils.getSet("-", "--",
			// ":"), ss.getTokens()));

			for (Sentence subSentence : subSentences) {
				List<Token> tokens = subSentence.getTokens();
				List<Integer> preps = findPrepositions(tokens);

				// no prepositions
				if (preps.isEmpty()) {
					continue;
				}

				for (Integer prepIdx : preps) {
					Sentence subStnc1 = new Sentence(".1", tokens.subList(0, prepIdx));
					Sentence subStnc2 = new Sentence(".2", tokens.subList(prepIdx + 1, tokens.size()));

					boolean isStnc1Negative = isNegative(subStnc1);
					boolean isStnc2Negative = isNegative(subStnc2);

					if (isStnc1Negative) {
						List<Integer> verbs = findProblematicVerbs(subStnc2.getTokens());
						if (!verbs.isEmpty()) {
							continue;
						}

						return 1;
					} else if (isStnc2Negative) {
						List<Integer> verbs = findProblematicVerbs(subStnc1.getTokens());
						if (!verbs.isEmpty()) {
							continue;
						}

						return 1;
					}

				}

				// List<Sentence> phrases =
				// SentenceUtils.findSubSentences(subSentence, preps);
				//
				//
				//
				// String txt = TextProcessor.getStringFromLemmas(subSentence);
				// if (txt.contains("with")) {
				// System.out.println(txt);
				// }
				//// if (phrases.size()>2) {
				//// System.out.println(TextProcessor.getStringFromLemmas(subSentence));
				//// }
				//// System.out.println(phrases.size());
				// for (Sentence phrase : phrases) {
				//// System.out.println(phrase);
				// if (isNegative(phrase)) {
				//// System.out.println(sentence);
				// return 1;
				// }
				// }
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
				// disregard the verb "build" if ends the sentence
				if (token.getLemma().equals("build") && i == tokens.size() - 1) {
					add = false;
				} else
				// disregard the verb "build" precedes the word "panel"
				if (token.getLemma().equals("build") && i + 1 < tokens.size()
						&& tokens.get(i + 1).getLemma().equals("panel")) {
					add = false;
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
		PatternMatcher pattern = findFirstPatternThatMatches(sentence, NEGATIVE_PMS);
		// debugging msgs
		// if (pattern != null) {
		// System.out.println("match: " + pattern.getClass().getSimpleName());
		// System.out.println(sentence);
		// return true;
		// }

		return pattern != null;
	}

}
