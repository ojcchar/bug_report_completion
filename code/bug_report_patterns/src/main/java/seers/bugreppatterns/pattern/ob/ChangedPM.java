package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ChangedPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		//find the "change" verb in past
		Integer changeVerbIdx = getChangeVerb(sentence);
		if (changeVerbIdx == -1) {
			return 0;
		}

		//no other verbs are accepted
		boolean hasOtherVerbs = hasOtherVerbs(sentence);
		if (hasOtherVerbs) {
			return 0;
		}

		//is the first verb of the sentence?
		if (changeVerbIdx - 1 < 0) {
			return 0;
		}

		Token prevToken = sentence.getTokens().get(changeVerbIdx - 1);

		// case: "it changed"
		if (prevToken.getLemma().equals("it") || prevToken.getGeneralPos().equals("NN")) {
			return 1;
		} else
		// case: "it has changed"
		if (prevToken.getLemma().equals("have") && changeVerbIdx - 2 >= 0) {
			Token prevToken2 = sentence.getTokens().get(changeVerbIdx - 2);

			if (prevToken2.getLemma().equals("it") || prevToken2.getGeneralPos().equals("NN")) {
				return 1;
			}
		}

		return 0;
	}

	private boolean hasOtherVerbs(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (!token.getLemma().equals("change") && token.getGeneralPos().equals("VB")) {

				// accept cases like "it has changed"
				if (!token.getLemma().equals("have") && i + 1 < tokens.size() && (tokens.get(i + 1).getLemma()
						.equals("change")
						&& (tokens.get(i + 1).getPos().equals("VBD") || tokens.get(i + 1).getPos().equals("VBN")))) {
					return true;
				}
			}
		}
		return false;
	}

	private Integer getChangeVerb(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();

		int changeIdx = -1;
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("change")) {
				if (token.getPos().equals("VBD") || token.getPos().equals("VBN")) {
					changeIdx = i;
				}
			}
		}
		return changeIdx;
	}

}
