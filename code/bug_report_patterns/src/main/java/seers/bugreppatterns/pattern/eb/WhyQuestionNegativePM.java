package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WhyQuestionNegativePM extends ExpectedBehaviorPatternMatcher {

	private final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		for (Sentence ss : SentenceUtils.breakByParenthesis(sentence)) {
			List<Sentence> subSentences = SentenceUtils.findSubSentences(ss,
					SentenceUtils.findLemmasInTokens(JavaUtils.getSet(":", ";", ",", "_"), ss.getTokens()));

			for (Sentence subSentence : subSentences) {
				List<Token> tokens = subSentence.getTokens();

				if (!tokens.isEmpty()){
					Token firstToken = tokens.get(0);
					Token lastToken = tokens.get(tokens.size() - 1);
	
					if (firstToken.getLemma().equalsIgnoreCase("why") && lastToken.getLemma().equalsIgnoreCase("?")) {
						if (tokens.size() - 2 > 1) {
							Sentence phrase = new Sentence(subSentence.getId(), tokens.subList(1, tokens.size() - 2));
	
							if (isNegative(phrase) || isNegativeEBModal(phrase)) {
								return 1;
							}
						}
	
					}
				}
			}
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

	private boolean isNegativeEBModal(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> modals = SentenceUtils.findLemmasInTokens(JavaUtils.getSet("must", "should", "need"), tokens);

		for (Integer modal : modals) {
			if (modal + 1 < tokens.size()) {
				Token next = tokens.get(modal + 1);
				if (next.getLemma().equals("not")) {
					return true;
				}
			}
		}
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

}
