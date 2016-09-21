package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SeemsToNegativeVerbPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		for (Integer seemsIndex : findSeemVerbs(tokens)) {
			if (seemsIndex != -1 && tokens.get(seemsIndex + 1).getLemma().equals("to")) {
				// check that is not SeemsToBe

				PatternMatcher pm = new SeemsToBePM();
				int match = pm.matchSentence(sentence);
				if (match == 0) {
					if (tokens.get(seemsIndex + 2).getLemma().equals("not")
							&& tokens.get(seemsIndex + 3).getGeneralPos().equals("VB")) {
						return 1;
					} else if (tokens.get(seemsIndex + 2).getLemma().equals("only")
							&& tokens.get(seemsIndex + 3).getLemma().equals("not")
							&& tokens.get(seemsIndex + 4).getGeneralPos().equals("VB")) {
						return 1;

					} else {
						Token tok = tokens.get(seemsIndex + 2);
						if (tok.getLemma().equals("only")) {
							Token nextToken = tokens.get(seemsIndex + 3);
							if (nextToken.getGeneralPos().equals("VB")) {
								if (SentenceUtils.lemmasContainToken(NegativeTerms.VERBS, nextToken)) {
									return 1;
								}
							}
						} else {
							if (tok.getGeneralPos().equals("VB")) {
								if (SentenceUtils.lemmasContainToken(NegativeTerms.VERBS, tok)) {
									return 1;
								}
							} else if (tok.getGeneralPos().equals("RB")) {
								if (SentenceUtils.lemmasContainToken(NegativeTerms.ADVERBS, tok)) {
									return 1;
								}
							}
						}

					}
				}
				if (tokens.get(seemsIndex + 2).getLemma().equals("get")) {
					Token nextToken = tokens.get(seemsIndex + 3);
					if (SentenceUtils.lemmasContainToken(NegativeTerms.VERBS, nextToken)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private List<Integer> findSeemVerbs(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(SeemsPM.SEEM_VERBS, tokens);
	}

}
