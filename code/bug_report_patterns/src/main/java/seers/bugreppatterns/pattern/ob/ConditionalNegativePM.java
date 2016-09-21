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

public class ConditionalNegativePM extends ObservedBehaviorPatternMatcher {

	private static final Set<String> PUNCTUATION = JavaUtils.getSet(",", "_");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> conditionalIndexes = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, tokens);

		// no conditionals
		if (conditionalIndexes.isEmpty()) {
			return 0;
		}

		// split sentences based on conditionals
		List<Sentence> subSentences = SentenceUtils.findSubSentences(sentence, conditionalIndexes);

		// if there is a sentence before the conditional term, skip it ->
		// the focus is on what is after the
		// conditional
		for (int i = conditionalIndexes.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {

			Sentence subSentence = subSentences.get(i);
			List<Token> subStncTokens = subSentence.getTokens();

			// find the punctuation in the sub-sentence
			List<Integer> punct = SentenceUtils.findLemmasInTokens(PUNCTUATION, subStncTokens);
			// findPunctuation(subStncTokens);

			// hard case: there is no punctuation. Try subsentences from end
			// to beginning. Check there is
			// something before the negative sentence.
			if (punct.isEmpty()) {
				boolean isNeg = false;
				for (int j = subStncTokens.size() - 1; j > 0; j--) {
					Sentence negSent = new Sentence(subSentence.getId(),
							subStncTokens.subList(j, subStncTokens.size()));

					if (isNegative(negSent)) {
						isNeg = true;
						break;
					}

				}
				if (isNeg && subStncTokens.size() > 1 && !findVerbs(subStncTokens).isEmpty()) {
					return 1;
				}

			}
			// The easy case: there is punctuation (',', '_', '-'). Make
			// sure that (i) there is something
			// between the conditional and the punctuation, and (ii) there
			// is a negative sentence after
			// the punctuation.
			else {
				List<Sentence> subSubSentences = SentenceUtils.findSubSentences(subSentence, punct);
				if (!subSubSentences.isEmpty()) {

					if (!subSubSentences.get(0).getTokens().isEmpty()) {
						for (int j = 1; j < subSubSentences.size(); j++) {

							if (isNegative(subSubSentences.get(j))) {
								return 1;
							}

						}
					}

				}
			}

		}

		// If there's at least one conditional and none of the sub-sentences are
		// negative
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		PatternMatcher pattern = findFirstPatternThatMatches(sentence, ButNegativePM.NEGATIVE_PMS);
		// debugging msgs
		// if (pattern != null) {
		// System.out.println(pattern.getClass().getSimpleName());
		// return true;
		// }

		return pattern != null;
	}

	private List<Integer> findVerbs(List<Token> tokens) {
		List<Integer> verbs = new ArrayList<>();
		boolean containsAux = false;
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			boolean add = true;
			if (token.getGeneralPos().equals("VB")) {
				if (i == 0) {
					add = false;
				} else if (token.getLemma().equals("be")
						|| token.getLemma().equals("have") && !token.getPos().equals("VB")) {
					containsAux = true;
				} else if ((token.getPos().equals("VBG") || token.getPos().equals("VBN")) && containsAux) {
					add = false;
				} else if (i - 1 >= 0 && tokens.get(i - 1).getWord().equalsIgnoreCase("to")
						&& token.getPos().equals("VB")) {
					add = false;
				}
				if (add) {
					verbs.add(i);
				}
			}
		}
		return verbs;
	}

}