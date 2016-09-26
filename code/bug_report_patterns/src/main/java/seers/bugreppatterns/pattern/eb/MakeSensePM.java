package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class MakeSensePM extends ExpectedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// no negative sentences
		if (isNegative(sentence)) {
			return 0;
		}

		// no questions
		if (SentenceUtils.isQuestion(sentence)) {
			return 0;
		}

		// -----------------------------

		List<Token> tokens = sentence.getTokens();
		List<Integer> senseTokens = findMainTokens(tokens);
		for (Integer senseTok : senseTokens) {
			int make = findMake(tokens.subList(0, senseTok));
			if (make != -1) {
				if (make - 1 >= 0) {
					Token prevToken = tokens.get(make - 1);
					if (prevToken.getLemma().equals("would")) {
						return 1;
					}
				}

				for (int i = make + 1; i < senseTok && i < tokens.size(); i++) {
					Token token = tokens.get(i);
					if (token.getLemma().equals("more") || token.getLemma().equals("most")) {
						return 1;
					}
				}
				if (make == senseTok - 1) {
					return 1;
				}
			}
		}
		return 0;
	}

	private int findMake(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("make")
					&& (token.getPos().equals("VB") || token.getPos().equals("VBZ") || token.getPos().equals("VBP"))) {
				return i;
			}
		}
		return -1;
	}

	private List<Integer> findMainTokens(List<Token> tokens) {

		List<Integer> mainToks = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("sense") && token.getGeneralPos().equals("NN")) {
				mainToks.add(i);
			}
		}
		return mainToks;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
