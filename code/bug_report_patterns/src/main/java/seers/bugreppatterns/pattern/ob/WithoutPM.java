package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class WithoutPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
		
		for (Sentence clause : clauses) {
			String txt = TextProcessor.getStringFromLemmas(clause);
			if ((txt.contains("without") || txt.contains("with no ")) && !isEBModal(clause.getTokens())) {
				return 1;
			}
		}
		return 0;
	}

}
