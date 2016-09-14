package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LeadsToPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new ErrorNounPhrasePM() };

	public final static Set<String> CAUSE_VERBS = JavaUtils.getSet("cause", "produce", "yield", "result", "lead");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		int indexVerb = indexVerbTokens(tokens);
		// check for results in interpreted as noun and not as verb
		if (indexVerb == -1) {
			indexVerb = indexResultVerbAsNoun(tokens);
		}
		if (indexVerb < (tokens.size() - 1) && indexVerb >= 0) {
			Sentence first = new Sentence(sentence.getId(), tokens.subList(0, indexVerb));
			Sentence second = new Sentence(sentence.getId(), tokens.subList(indexVerb + 1, tokens.size()));
			// check that in the first sentence there is a noun or pronoun at
			// least or terms are this
			List<Token> tokensFirst = first.getTokens();
			boolean isSubject = false;
			for (int i = 0; i < tokensFirst.size(); i++) {
				if (tokensFirst.get(i).getGeneralPos().equals("NN") || tokensFirst.get(i).getGeneralPos().equals("PRP")
						|| tokensFirst.get(i).getGeneralPos().equals("DT")
						|| tokensFirst.get(i).getGeneralPos().equals("WH")
						|| tokensFirst.get(i).getGeneralPos().equals("JJ")) {
					isSubject = true;
				}
			}
			// check that the second sentence is not negative
			if (isSubject || indexVerb == 0) {

				if (!isNegative(second)) {
					return 1;
				}
			}
		}
		return 0;
	}

	private int indexVerbTokens(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB") && SentenceUtils.lemmasContainToken(CAUSE_VERBS, token)) {
				return i;
			}
		}
		return -1;
	}

	private int indexResultVerbAsNoun(List<Token> tokens) {
		for (int i = 0; i < tokens.size() - 1; i++) {
			if (tokens.get(i).getLemma().equals("result") && tokens.get(i + 1).getLemma().equals("in")) {
				return i;
			}
		}
		return -1;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
