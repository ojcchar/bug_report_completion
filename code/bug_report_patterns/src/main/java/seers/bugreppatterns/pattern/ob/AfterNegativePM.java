package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AfterNegativePM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM() };

	public final static Set<String> AFTER = JavaUtils.getSet("after");

	private static final Set<String> PERIOD = JavaUtils.getSet(".");
	private static final Set<String> PUNCTUATION = JavaUtils.getSet(",", "_", "-");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// System.out.println(TextProcessor.getStringFromTerms(sentence));
		List<Token> tokens = sentence.getTokens();

		// split sentences based on "."
		List<Sentence> superSentences = SentenceUtils.findSubSentences(sentence, findPeriod(tokens));

		for (Sentence superSentence : superSentences) {

			List<Integer> afters = SentenceUtils.findLemmasInTokens(AFTER, superSentence.getTokens());

			if (!afters.isEmpty()) {
				// split sentences based on "after"
				List<Sentence> subSentences = SentenceUtils.findSubSentences(superSentence, afters);

				// if there is a sentence before the "after" term, skip it -> the focus is on what is after the "after"
				for (int i = afters.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {
					Sentence subSentece = subSentences.get(i);

					List<Integer> punct = findPunctuation(subSentece.getTokens());

					// hard case: there is no punctuation. Try subsentences from end to beginning. Check there is
					// something before the negative sentence.
					if (punct.isEmpty()) {

						for (int j = subSentece.getTokens().size() - 1; j >= 0; j--) {
							Sentence negSent = new Sentence(subSentece.getId(),
									subSentece.getTokens().subList(j, subSentece.getTokens().size()));
							if (isNegative(negSent) && j > 1) {
								return 1;
							}

						}

					}
					// The easy case: there is punctuation (',', '_', '-'). Make sure that (i) there is something
					// between the "after" and the punctuation, and (ii) there is a negative sentence after
					// the punctuation.
					else {
						List<Sentence> subSubSentences = SentenceUtils.findSubSentences(subSentece, punct);
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
		}
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
		if (symbols.size() - 1 >= 0 && symbols.get(symbols.size() - 1) == tokens.size() - 1) {
			return symbols.subList(0, symbols.size() - 1);
		}
		return symbols;
	}

	private List<Integer> findPeriod(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(PERIOD, tokens);
	}

}
