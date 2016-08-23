package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AfterNegativePM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM() };

	public final static PatternMatcher[] DOUBLE_NEG = { new ButNegativePM() };

	public final static String[] AFTER = { "after" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		// split sentences based on "."
		List<Sentence> superSentences = findSubSentences(sentence, findPeriod(tokens));

		for (Sentence superSentence : superSentences) {

			List<Integer> afters = findTermsInTokens(AFTER, superSentence.getTokens());

			if (!afters.isEmpty()) {
				// split sentences based on "after"
				List<Sentence> subSentences = findSubSentences(superSentence, afters);

				// if there is a sentence before the "after" term, skip it.
				for (int i = afters.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {
					Sentence subSentece = subSentences.get(i);

					List<Integer> punct = findPunctuation(subSentece.getTokens());

					// if there is no punctuation, more than one verb in the sentence would indicate that right after
					// the "after" there is a sentence and not a noun
					if (punct.isEmpty()) {
						List<Integer> verbs = findVerbs(subSentece.getTokens());
						if (verbs.size() > 1) {
							for (PatternMatcher pm : NEGATIVE_PMS) {
								int match = pm.matchSentence(subSentences.get(i));
								if (match == 1) {
									return 1;
								}
							}
						}
					}
					// if there is punctuation, make sure that there is a sentence (not just nouns) right after the
					// "after", and a negative sentence after that. We discount phrases that would make a double
					// negation such as the second part of "After tabbing or otherwise navigating through the
					// autocomplete list for the location bar, the last entry selected appears in the location bar but
					// is not loaded (this is correct)."
					else {
						List<Sentence> subSubSentences = findSubSentences(subSentece, punct);
						if (!findVerbs(subSubSentences.get(0).getTokens()).isEmpty()) {
							for (int j = 1; j < subSubSentences.size(); j++) {
								boolean notDoubleNeg = true;
								for (PatternMatcher pm : DOUBLE_NEG) {
									int match = pm.matchSentence(subSubSentences.get(j));
									if (match == 1) {
										notDoubleNeg = false;
									}
								}
								if (notDoubleNeg) {
									for (PatternMatcher pm : NEGATIVE_PMS) {
										int match = pm.matchSentence(subSubSentences.get(j));
										if (match == 1) {
											return 1;
										}
									}
								}
							}
						}
					}

				}
			}
		}
		return 0;
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals(",") || token.getWord().equals("_") || token.getWord().equals("-")) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	private List<Integer> findPeriod(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals(".")) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	private List<Integer> findVerbs(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				// ignore the to-infinitive form
				if (i - 1 >= 0 && tokens.get(i - 1).getWord().equalsIgnoreCase("to")) {
					continue;

				}
				symbols.add(i);
			}
		}
		return symbols;
	}

}
