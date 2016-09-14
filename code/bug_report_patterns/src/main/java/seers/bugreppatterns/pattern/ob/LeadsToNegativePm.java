package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LeadsToNegativePm extends ObservedBehaviorPatternMatcher {

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
						|| tokensFirst.get(i).getGeneralPos().equals("WH")) {
					isSubject = true;
				}
			}

			// check that the second sentence is negative
			if (isSubject || indexVerb == 0) {
				if (isNegative(second)) {
					return 1;
				}

			}
		}
		return 0;
	}

	private int indexVerbTokens(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB") && SentenceUtils.lemmasContainToken(LeadsToPM.CAUSE_VERBS, token)) {
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
		return sentenceMatchesAnyPatternIn(sentence, LeadsToPM.NEGATIVE_PMS);
	}
}
