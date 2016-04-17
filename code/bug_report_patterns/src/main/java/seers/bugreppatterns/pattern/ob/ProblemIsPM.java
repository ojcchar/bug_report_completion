package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ProblemIsPM extends ObservedBehaviorPatternMatcher{

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String text = TextProcessor.getStringFromLemmas(sentence);
		if(text.startsWith("the problem be")||text.startsWith("the issue be")||text.startsWith("my problem be")){
			PatternMatcher pm = new ProblemInPM();
			int match = pm.matchSentence(sentence);
			if(match==0){
				return 1;
			}
		}
		return 0;
	}

}
