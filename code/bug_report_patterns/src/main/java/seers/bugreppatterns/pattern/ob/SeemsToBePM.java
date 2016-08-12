package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class SeemsToBePM extends ObservedBehaviorPatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// TODO Auto-generated method stub
		String txt = TextProcessor.getStringFromLemmas(sentence);
		if(txt.contains("seem to be") || txt.contains("appear to be")){
			return 1;
		}
		return 0;
	}

}
