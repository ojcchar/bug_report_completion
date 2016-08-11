package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ButPM extends ObservedBehaviorPatternMatcher{

	final private static String[] CONTRAST_TERM = {"but","although", "however", "nevertheless", "though", "yet"};
	
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens2 = sentence.getTokens();
		List<Integer> buts = findContrastTerm(tokens2);
		for (Integer but : buts) {
			Sentence sentence2 = new Sentence(sentence.getId(), tokens2.subList(but, tokens2.size()));
			for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
				int match = pm.matchSentence(sentence2);
				if (match == 0) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	private List<Integer> findContrastTerm(List<Token> tokens) {

		Set<String> setContrastTerm = new HashSet<String>(Arrays.asList(CONTRAST_TERM));
		
		List<Integer> but = new ArrayList<>();
		
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if(setContrastTerm.contains(token.getLemma())){
				but.add(i);
			}
		}
		return but;
	}

}
