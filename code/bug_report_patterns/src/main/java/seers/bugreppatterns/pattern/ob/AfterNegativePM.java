package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AfterNegativePM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM() };

	public final static PatternMatcher[] DOUBLE_NEG = { new ButNegativePM() };

	public final static String[] AFTER = { "after" };

	public final static String[] TIME_TERMS = { "second", "minute", "hour", "day", "week", "month", "year" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		//System.out.println(TextProcessor.getStringFromTerms(sentence));
		List<Token> tokens = sentence.getTokens();

		// split sentences based on "."
		List<Sentence> superSentences = findSubSentences(sentence, findPeriod(tokens));

		for (Sentence superSentence : superSentences) {

			List<Integer> afters = findTermsInTokens(AFTER, superSentence.getTokens());

			if (!afters.isEmpty()) {
				// split sentences based on "after"
				List<Sentence> subSentences = findSubSentences(superSentence, afters);

				// if there is a sentence before the "after" term, skip it -> the focus is on what is after the "after"
				for (int i = afters.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {
					Sentence subSentece = subSentences.get(i);

					List<Integer> punct = findPunctuation(subSentece.getTokens());

					// hard case: there is no punctuation. Try subsentences from end to beginning. Check there is
					// something before the negative sentence.
					// Special case: don't consider negative sentences that come after contrast terms: "After doing
					// this, the last entry selected appears in the location bar but is not loaded (this is correct)."
					if (punct.isEmpty()) {
						boolean isButNegSentence = false;
						for (PatternMatcher pm : DOUBLE_NEG) {
							int match = pm.matchSentence(subSentece);
							if (match == 1) {
								isButNegSentence = true;
							}
						}
						if (!isButNegSentence) {
							for (int j = subSentece.getTokens().size() - 1; j >= 0; j--) {
								Sentence negSent = new Sentence(subSentece.getId(),
										subSentece.getTokens().subList(j, subSentece.getTokens().size()));
								for (PatternMatcher pm : NEGATIVE_PMS) {
									int match = pm.matchSentence(negSent);
									if (match == 1 && j > 1) {
										return 1;
									}
								}
							}
						}

					}
					// The easy case: there is punctuation (',', '_', '-'). Make sure that (i) there is something
					// between the "after" and the punctuation, and (ii) there is a negative sentence after
					// the punctuation.
					// Special case: same as before"
					else {
						List<Sentence> subSubSentences = findSubSentences(subSentece, punct);
						if (!subSubSentences.get(0).getTokens().isEmpty()) {
							for (int j = 1; j < subSubSentences.size(); j++) {

								boolean isButNegSentence = false;
								for (PatternMatcher pm : DOUBLE_NEG) {
									int match = pm.matchSentence(subSubSentences.get(j));
									if (match == 1) {
										isButNegSentence = true;
									}
								}
								if (!isButNegSentence) {
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
		for (int i = 0; i < tokens.size() - 1; i++) {
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

}
