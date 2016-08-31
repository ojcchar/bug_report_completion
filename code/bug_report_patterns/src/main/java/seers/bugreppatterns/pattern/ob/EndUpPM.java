package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class EndUpPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.matches(".+[^A-Za-z]?end(ed|s|ing)? up[^A-Za-z].+") && !isEBModal(sentence.getTokens())
				&& !containsWould(sentence.getTokens())) {
			return 1;
		}

		return 0;
	}

	private boolean containsWould(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getGeneralPos().equals("MD") && current.getLemma().equals("would")) {
				return true;
			} else if (current.getGeneralPos().equals("NN") && current.getLemma().equals("d") && i - 1 >= 0) {
				Token previous = tokens.get(i - 1);
				if(previous.getGeneralPos().equals("NN") || previous.getGeneralPos().equals("PRP")) {
					return true;
				}
			}
		}
		return false;
	}
}
