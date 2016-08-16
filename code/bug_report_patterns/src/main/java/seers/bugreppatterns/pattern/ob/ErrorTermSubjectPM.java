package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorTermSubjectPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		// contains an error terms
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if ((Arrays.stream(VerbErrorPM.ERROR_TERMS).anyMatch(t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("NN"))
					|| (Arrays.stream(NegativeTerms.ADJECTIVES).anyMatch(t -> token.getLemma().contains(t))
							&& token.getGeneralPos().equals("JJ"))
					|| (Arrays.stream(NegativeTerms.ADVERBS).anyMatch(t -> token.getLemma().contains(t))
							&& token.getGeneralPos().equals("RB"))
					|| compoundErrorTerms(tokens) || NounsAsVerbs(tokens)) {
				// is not error how and error noun phrase
				PatternMatcher pm = new ErrorHowPM();
				int match = pm.matchSentence(sentence);
				if (match == 0) {
					pm = new ErrorNounPhrasePM();
					match = pm.matchSentence(sentence);
					if (match == 0) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	public boolean compoundErrorTerms(List<Token> tokens) {
		for (int i = 0; i < (tokens.size() - 1); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("NN") && token.getLemma().equals("blank")
					&& tokens.get(i + 1).getGeneralPos().equals("NN") && tokens.get(i + 1).getLemma().equals("page")) {
				return true;
			}
		}
		return false;
	}

	public boolean NounsAsVerbs(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB") && (token.getLemma().equals("double"))) {
				return true;
			}
		}
		return false;

	}

}
