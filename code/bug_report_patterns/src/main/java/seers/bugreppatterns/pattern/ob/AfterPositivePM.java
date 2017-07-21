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

public class AfterPositivePM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new VerbToBeNegativePM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new ButNegativePM() };

	public final static PatternMatcher[] DOUBLE_NEG = { new ButNegativePM() };

	public final static Set<String> AFTER = JavaUtils.getSet("after");

	private static final Set<String> PUNCTUATION = JavaUtils.getSet(",", "_", "-");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// split sentences based on "."
		List<Sentence> superSentences = SentenceUtils.breakByParenthesis(sentence);

		for (Sentence superSentence : superSentences) {

			List<Integer> afters = SentenceUtils.findLemmasInTokens(AFTER, superSentence.getTokens());

			if (!afters.isEmpty()) {
				// split sentences based on "after"
				List<Sentence> subSentences = SentenceUtils.findSubSentences(superSentence, afters);

				// if there is a sentence before the "after" term, skip it ->
				// the focus is on what is after the "after"
				for (int i = afters.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {
					Sentence subSentence = subSentences.get(i);

					List<Integer> punct = findPunctuation(subSentence.getTokens());

					// hard case: there is no punctuation. Try subsentences from
					// end to beginning.
					if (punct.isEmpty()) {
						for (int j = subSentence.getTokens().size() - 1; j > 0; j--) {
							Sentence negSent = new Sentence(subSentence.getId(),
									subSentence.getTokens().subList(j, subSentence.getTokens().size()));

							if (isNegative(negSent)) {
								return 0;
							}

						}

						// make sure there is some clause proceeding the "after"
						// cases like "something happens after clicking" are not
						// accepted. Two verbs might indicate the presence of
						// two clauses. That is why the condition is '>1'
						boolean hasAtLeastTwoClauses = findValidVerbs(subSentence.getTokens()).size() > 1;
						if (subSentence.getTokens().size() > 1 && hasAtLeastTwoClauses
								&& !SentenceUtils.sentenceContainsAnyLemmaIn(subSentence, AfterTimePM.TIME_TERMS)) {
							return 1;
						}

					}
					// The easy case: there is punctuation (',', '_', '-'). Make
					// sure that (i) there is something
					// between the "after" and the punctuation, and (ii) there
					// is no negative sentence after
					// the punctuation.
					else {
						List<Sentence> subSubSentences = SentenceUtils.findSubSentences(subSentence, punct);
						if (!subSubSentences.get(0).getTokens().isEmpty()) {
							boolean isPos = true;
							for (int j = 1; j < subSubSentences.size(); j++) {

								if (isNegative(subSubSentences.get(j))) {
									isPos = false;
									break;
								}

							}
							if (isPos) {
								return 1;
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

	private List<Integer> findValidVerbs(List<Token> tokens) {
		List<Integer> verbs = new ArrayList<>();
		boolean containsAux = false;
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			boolean add = true;
			if (token.getGeneralPos().equals("VB")
					|| SentenceUtils.matchTermsByLemma(SentenceUtils.UNDETECTED_VERBS, token)
					|| token.getLemma().equalsIgnoreCase("typing")) {
				// if (i == 0) {
				// add = false;
				// } else
				if ((token.getLemma().equals("be") || token.getLemma().equals("have"))
						&& !token.getPos().equals("VB")) {
					containsAux = true;
				} else if ((token.getPos().equals("VBG") || token.getPos().equals("VBN")) && containsAux) {
					add = false;
				} else if (i - 1 >= 0 && tokens.get(i - 1).getWord().equalsIgnoreCase("to")
						&& token.getPos().equals("VB")) {
					add = false;
				} else if (i - 1 == 0 && tokens.get(i - 1).getGeneralPos().equals("PRP")) {
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
