package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextPreprocessor;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeAdjInPM extends ObservedBehaviorPatternMatcher{

	final public static String [] TOKENS = {"in","on","for"};
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens=sentence.getTokens();
		Token start = tokens.get(0);
		if (Arrays.stream(NegativeAdjOrAdvPM.NEGATIVE_ADJ).anyMatch(p -> start.getLemma().equals(p))) {
			//verify location adverb
			String text = TextProcessor.getStringFromLemmas(sentence);
			for (int i=1;(i<tokens.size()-1);i++){
				Token token = tokens.get(i);
				if (Arrays.stream(TOKENS).anyMatch(p -> token.getLemma().equals(p))){
					return 1;
				}
			}
			if(tokens.size()>1){
				return 1;
			}
		}
		return 0;
	}

}
