package seers.bugreppatterns.pattern.ob;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class PassiveVoicePM extends ObservedBehaviorPatternMatcher {

	private final static Set<String> AUXILIARS = JavaUtils.getSet("be", "get");

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new VerbToBeNegativePM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new NoLongerPM() };

	private final static Set<String> FORBIDDEN_TERMS = new HashSet<>(CONDITIONAL_TERMS);
	{
		FORBIDDEN_TERMS.addAll(CONTRAST_TERMS);
		FORBIDDEN_TERMS.addAll(JavaUtils.getSet("after", "because"));
	}

	public static final Set<String> PUNCTUATION = JavaUtils.getSet(";", "-", "_", "--");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (isPassive(tokens) && !isEBModal(tokens) && !isNegative(sentence)
				&& !SentenceUtils.sentenceContainsAnyLemmaIn(sentence, FORBIDDEN_TERMS)) {
			return 1;
		}

		return 0;
	}

	private boolean isPassive(List<Token> tokens) {
		int i = 0;
		boolean containsAuxiliar = false;
		int par = 0;
		while (i < tokens.size()) {
			Token current = tokens.get(i);
			if (current.getGeneralPos().equals("VB") && SentenceUtils.lemmasContainToken(AUXILIARS, current)
					&& par == 0) {
				containsAuxiliar = true;
				i++;
				break;
			} else if (current.getLemma().equals("-lrb-")) {
				par++;
			} else if (current.getLemma().equals("-rrb-")) {
				par--;
			}
			i++;
		}
		while (containsAuxiliar && i < tokens.size()) {
			Token current = tokens.get(i);
			if ((current.getPos().equals("VBN") || current.getPos().equals("VBD")) && par == 0) {
				return true;
			} else if (SentenceUtils.lemmasContainToken(PUNCTUATION, current)) {
				return false;
			} else if (current.getLemma().equals("-lrb-")) {
				par++;
			} else if (current.getLemma().equals("-rrb-")) {
				par--;
			}
			i++;
		}
		return false;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
