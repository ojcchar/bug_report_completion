package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NothingHappensPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromLemmas(sentence);
		if ((txt.toLowerCase().matches(".*[^A-Za-z]?nothing happen[^A-Za-z]?.*")) && !isEBModal(sentence.getTokens())) {
			return 1;
		}
		return 0;
	}

	private boolean isEBModal(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token tok = tokens.get(i);
			if (tok.getGeneralPos().equals("MD") && (tok.getLemma().equals("must") || tok.getLemma().equals("need")
					|| tok.getLemma().equals("should"))) {
				return true;
			}
		}
		return false;
	}

}
