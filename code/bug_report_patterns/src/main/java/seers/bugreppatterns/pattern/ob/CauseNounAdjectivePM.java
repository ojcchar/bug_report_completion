package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CauseNounAdjectivePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		int indexVerb = indexVerbTokens(tokens);
		if (indexVerb >= 0 && indexVerb < (tokens.size() - 1)) {
			Sentence first = new Sentence(sentence.getId(), tokens.subList(0, indexVerb));
			Sentence second = new Sentence(sentence.getId(), tokens.subList(indexVerb + 1, tokens.size()));
			// check ErrorNounPhrase
			PatternMatcher pm = new ErrorNounPhrasePM();
			int match = pm.matchSentence(second);
			// negative noun phrase with predicate eventually 
			if (isNegativeNounPhrase(second) || isNegativeNounPhrase(first) || (match==1)) {
				return 1;
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

	private boolean isNegativeNounPhrase(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(VerbErrorPM.ERROR_TERMS).anyMatch(t -> t.equals(token.getLemma()))
					&& token.getGeneralPos().equals("NN")) {
				return true;
			} else if (Arrays.stream(NegativeAdjOrAdvPM.NEGATIVE_ADJ).anyMatch(t -> t.equals(token.getLemma()))
					&& token.getGeneralPos().equals("JJ")) {
				return true;
			} else if (Arrays.stream(NegativeAdjOrAdvPM.NEGATIVE_ADV).anyMatch(t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("RB")) {
				return true;
			}
		}
		return false;
	}
}
