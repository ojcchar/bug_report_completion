package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
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
				for (PatternMatcher pm : LeadsToPM.NEGATIVE_PMS) {
					int match = pm.matchSentence(second);
					if (match == 1) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private int indexVerbTokens(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")
					&& Arrays.stream(LeadsToPM.CAUSE_VERBS).anyMatch(t -> t.equals(token.getLemma()))) {
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

}
