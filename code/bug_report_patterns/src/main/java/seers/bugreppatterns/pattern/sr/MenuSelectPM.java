package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class MenuSelectPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String txt = TextProcessor.getStringFromLemmas(sentence);
		return txt.matches("(?s).*select.*(menu|option).*") ? 1 : 0;
	}

}
