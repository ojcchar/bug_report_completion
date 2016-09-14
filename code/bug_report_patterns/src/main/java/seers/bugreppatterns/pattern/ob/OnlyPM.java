package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.eb.MakeSensePM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class OnlyPM extends ObservedBehaviorPatternMatcher {

	private static final String ONLY = "only";

	private static final PatternMatcher[] EB_PMS = { new MakeSensePM() };

	private static Set<String> ALLOWED_PREPS = JavaUtils.getSet("that");

	private static Set<String> PRESENT_TENSE_VERBS = JavaUtils.getSet("crash", "build", "return", "copy");

	private static Set<String> PUNCTUATION = JavaUtils.getSet(",", ".", ";", ":", "--");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Integer> punctuation = findPunctuation(sentence.getTokens());
		List<Sentence> subSentences = SentenceUtils.findSubSentences(sentence, punctuation);

		for (Sentence subSentence : subSentences) {
			List<Token> tokens = subSentence.getTokens();
			if (containsOnly(tokens) && !isEBModal(tokens) && !isEB(subSentence)) {
				return 1;
			}
		}
		return 0;
	}

	private boolean containsOnly(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// find every "only"
			if ((current.getGeneralPos().equals("RB") || current.getGeneralPos().equals("JJ"))
					&& current.getLemma().equals(ONLY)) {

				// The right "only"
				Token previous = i - 1 >= 0 ? tokens.get(i - 1) : null;
				Token next = i + 1 < tokens.size() ? tokens.get(i + 1) : null;

				if (previous != null && previous.getGeneralPos().equals("IN")
						&& !SentenceUtils.lemmasContainToken(ALLOWED_PREPS, previous)) {
					continue;
				}
				// the one that comes before or after a verb
				if ((previous != null && previous.getGeneralPos().equals("VB"))
						|| (next != null && next.getGeneralPos().equals("VB"))
						// some verbs that are not correctly PoS tagged
						|| (previous != null && SentenceUtils.lemmasContainToken(PRESENT_TENSE_VERBS, previous)
								&& previous.getPos().equals("NNS"))
						|| (next != null && SentenceUtils.lemmasContainToken(PRESENT_TENSE_VERBS, next)
								&& next.getPos().equals("NNS"))) {
					return true;
				}

				// the one that precedes a verb in infinitive
				if (next != null && next.getGeneralPos().equals("TO")) {
					Token afterTo = i + 2 < tokens.size() ? tokens.get(i + 2) : null;

					if (afterTo != null && afterTo.getGeneralPos().equals("VB")) {
						return true;
					}
				}

			}
		}
		return false;
	}

	private boolean isEB(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, EB_PMS);
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
	}
}
