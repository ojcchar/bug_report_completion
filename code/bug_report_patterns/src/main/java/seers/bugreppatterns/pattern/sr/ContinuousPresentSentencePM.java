package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;

/**
 * Created by juan on 9/20/16.
 */
public class ContinuousPresentSentencePM extends StepsToReproducePatternMatcher {
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return sentence.getTokens().stream().filter(t -> t.getGeneralPos().equals("VB"))
				.allMatch(t -> t.getPos().equals("VBG") || t.getLemma().equals("be")) ? 1 : 0;
	}
}
