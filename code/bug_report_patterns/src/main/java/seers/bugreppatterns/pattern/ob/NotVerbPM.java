package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NotVerbPM extends ObservedBehaviorPatternMatcher {

	private final static String NOT = "not";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (tokens.size() >= 2) {
			Token firstToken = tokens.get(0);
			Token secondToken = tokens.get(1);
			
			if(firstToken.getLemma().equals(NOT) && secondToken.getPos().equals("VBG")) {
				return 1;
			}
		}
		return 0;
	}

}
