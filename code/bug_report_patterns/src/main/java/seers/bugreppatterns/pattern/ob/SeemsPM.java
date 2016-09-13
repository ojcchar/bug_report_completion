package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SeemsPM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> SEEM_VERBS =  JavaUtils.getSet( "seem", "appear", "look" );

	public final static PatternMatcher[] OTHER_SEEM = { new SeemsToNegativeVerbPM(), new SeemsToBePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		// Check that is not other seem pattern
		if (isOtherSeem(sentence)) {
			return 0;
		}

		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.matches(".*(appear|seem|look)(ed|s|ing)? (to|that|like|each|as|none)[^A-Za-z].*")) {
			return 1;
		}

		List<Token> tokens = sentence.getTokens();
		List<Integer> terms = findSeemVerbs(tokens);

		for (Integer term : terms) {
			if (term + 1 < tokens.size()) {
				Token nextToken = tokens.get(term + 1);
				if (nextToken.getGeneralPos().equals("NN") || nextToken.getGeneralPos().equals("RB")
						|| nextToken.getGeneralPos().equals("JJ") || nextToken.getGeneralPos().equals("DT")) {
					return 1;
				}
			}
		}
		return 0;
	}

	private List<Integer> findSeemVerbs(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(SEEM_VERBS, tokens);
	}

	private boolean isOtherSeem(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, OTHER_SEEM);
	}
}
