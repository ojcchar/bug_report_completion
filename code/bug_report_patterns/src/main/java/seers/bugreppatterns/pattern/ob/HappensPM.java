package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class HappensPM extends ObservedBehaviorPatternMatcher {

	final static private Set<String> VERBS = JavaUtils.getSet("happen", "occur");

	public HappensPM() {
	}

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> verbs = foundIndexTokens(tokens);
		if (verbs.size() > 0) {
			return 1;
		}

		return 0;
	}

	private ArrayList<Integer> foundIndexTokens(List<Token> tokens) {
		ArrayList<Integer> indexConditionalTerms = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (SentenceUtils.lemmasContainToken(VERBS, token) && token.getGeneralPos().equals("VB")) {
				indexConditionalTerms.add(i);
			}
		}
		return indexConditionalTerms;
	}

}
