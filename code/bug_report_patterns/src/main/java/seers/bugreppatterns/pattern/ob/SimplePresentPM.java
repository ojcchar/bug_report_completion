package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.sr.ActionsPresentPM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SimplePresentPM extends ObservedBehaviorPatternMatcher {

	private final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new NoLongerPM(), new PassiveVoicePM() };

	private final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet();

	private final static Set<String> FORBIDDEN_TERMS = CONDITIONAL_TERMS;
	{
		FORBIDDEN_TERMS.addAll(CONTRAST_TERMS);
		FORBIDDEN_TERMS.addAll(JavaUtils.getSet("after", "because"));
	}

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		for (Sentence subSentence : findSubsentences(sentence)) {

			SimpleTenseChecker checker = new SimpleTenseChecker(ActionsPresentPM.POS, SentenceUtils.UNDETECTED_VERBS,
					SimplePresentPM.EXCLUDED_VERBS);
			int numClauses = checker.countNumClauses(subSentence);

			if (numClauses > 0 && !isNegative(subSentence)
					&& !SentenceUtils.sentenceContainsAnyLemmaIn(subSentence, FORBIDDEN_TERMS)) {
				return 1;
			}
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

	public static List<Sentence> findSubsentences(Sentence sentence) {

		List<Sentence> subSentences = new ArrayList<Sentence>();
		Sentence subSentence = new Sentence(sentence.getId(), new ArrayList<Token>());
		List<Token> tokens = sentence.getTokens();

		for (int j = 0; j < tokens.size(); j++) {
			Token current = tokens.get(j);

			if (current.getLemma().equals(".")) {
				subSentences.add(subSentence);
				subSentence = new Sentence(sentence.getId(), new ArrayList<Token>());
			} else if (current.getLemma().equals("-lrb-")) {

				Sentence s1 = closingParSentence(sentence, j + 1);
				int s1size = s1.getTokens().size();

				int end = j + s1size + 1;
				if (end < tokens.size() && tokens.get(end).getLemma().equals("-rrb-")) {
					subSentences.add(s1);
					j = end;
				} else {
					subSentence.addToken(current);
				}
			} else {
				subSentence.addToken(current);
			}
		}

		if (subSentence != null && !subSentence.getTokens().isEmpty()) {
			subSentences.add(subSentence);
		}
		return subSentences;
	}

	private static Sentence closingParSentence(Sentence sentence, int start) {
		Sentence subSentence = new Sentence(sentence.getId(), new ArrayList<Token>());
		List<Token> tokens = sentence.getTokens();
		for (int j = start; j < tokens.size(); j++) {
			Token current = tokens.get(j);

			if (current.getLemma().equals("-rrb-")) {
				return subSentence;
			} else {
				subSentence.addToken(current);
			}
		}
		return subSentence;
	}

}
