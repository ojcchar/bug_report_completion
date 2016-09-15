package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class GerundSubjectPM extends ObservedBehaviorPatternMatcher {

	final private static Set<String> VERBS_AS_NOUNS = JavaUtils.getSet("copy", "drag", "insert");

	private static final Set<String> PUNCTUATION = JavaUtils.getSet(":", ";", ".");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> subSentences = SentenceUtils.findSubSentences(sentence, findPunctuation(sentence.getTokens()));

		for (Sentence subSentence : subSentences) {
			List<Token> tokens = subSentence.getTokens();
			Token first = tokens.get(0);
			if (first.getPos().equals("VBG")
					|| (first.getWord().toLowerCase().endsWith("ing") && first.getGeneralPos().equals("NN"))) {
				for (int i = 1; i < tokens.size(); i++) {
					Token token = tokens.get(i);
					if (token.getGeneralPos().equals("VB")) {
						return 1;
					} else if (token.getGeneralPos().equals("NN")
							&& SentenceUtils.lemmasContainToken(VERBS_AS_NOUNS, token)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
		if (symbols.size() - 1 >= 0 && symbols.get(symbols.size() - 1) == tokens.size() - 1) {
			return symbols.subList(0, symbols.size() - 1);
		}
		return symbols;
	}

}
