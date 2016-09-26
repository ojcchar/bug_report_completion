package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class DespitePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		int index = adverbIndex(tokens);
		if (index > 0) {
			List<Token> t1 = tokens.subList(0, index);
			List<Token> t2 = tokens.subList(index, tokens.size());
			boolean first = false;
			for (Token token : t1) {
				if (token.getGeneralPos().equals("VB")) {
					first = true;
				}
			}
			if (!first) {
				for (Token token : t2) {
					if (token.getGeneralPos().equals("VB")) {
						first = true;
					}
				}
			} else {
				return 1;
			}
		} else if (index == 0) {
			for (Token token : tokens) {
				if (token.getGeneralPos().equals("VB")) {
					return 1;
				}
			}
		}

		return 0;
	}

	private int adverbIndex(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getLemma().matches("although|despite")) {
				return i;
			} else if (i + 1 < tokens.size() && tokens.get(i).getLemma().equals("even")
					&& tokens.get(i + 1).getLemma().matches("if|though|after|when")) {
				return i;
			} else if (i + 2 < tokens.size() && tokens.get(i).getLemma().equals("in")
					&& tokens.get(i + 1).getLemma().equals("spite") && tokens.get(i + 2).getLemma().equals("of")) {
				return i;
			}
		}
		return -1;
	}

}
