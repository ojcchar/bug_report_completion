package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class AfterPositivePM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new ButNegativePM() };

	public final static PatternMatcher[] DOUBLE_NEG = { new ButNegativePM() };

	public final static String[] AFTER = { "after" };

	public final static String[] TIME_TERMS = { "second", "minute", "hour", "day", "week", "month", "year" };

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

				// if there is a sentence before the "after" term, skip it -> the focus is on what is after the "after"
				for (int i = afters.get(0) > 0 ? 1 : 0; i < subSentences.size(); i++) {
					Sentence subSentece = subSentences.get(i);

					List<Integer> punct = findPunctuation(subSentece.getTokens());

					// hard case: there is no punctuation. Try subsentences from end to beginning. Check there is
					// something before the non-negative sentence.
					if (punct.isEmpty()) {
						for (int j = subSentece.getTokens().size() - 1; j > 0; j--) {
							Sentence negSent = new Sentence(subSentece.getId(),
									subSentece.getTokens().subList(j, subSentece.getTokens().size()));

							if (isNegative(negSent)) {
								return 0;
							}

						}
						if (subSentece.getTokens().size() > 1 && !findVerbs(subSentece.getTokens()).isEmpty()
								&& findLemmasInTokens(TIME_TERMS, subSentece.getTokens()).isEmpty()) {
							return 1;
						}

					}
					// The easy case: there is punctuation (',', '_', '-'). Make sure that (i) there is something
					// between the "after" and the punctuation, and (ii) there is no negative sentence after
					// the punctuation.
					else {
						List<Sentence> subSubSentences = findSubSentences(subSentece, punct);
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
		for (PatternMatcher pm : NEGATIVE_PMS) {
			int match = pm.matchSentence(sentence);
			if (match == 1) {
				return true;
			}
		}
		return false;
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
		LinkedList<Character> pars = new LinkedList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals("-LRB-")) {
				pars.add('(');
			}
			if (token.getWord().equals("-RRB-")) {
				pars.removeLast();
			}
			if (token.getWord().equals(".") && pars.isEmpty()) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	private List<Integer> findVerbs(List<Token> tokens) {
		List<Integer> verbs = new ArrayList<>();
		boolean containsAux = false;
		for (int i = 2; i < tokens.size() - 1; i++) {
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
