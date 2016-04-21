package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class TriggerSentencePM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> toks = findMainTokens(tokens);

		if (toks.isEmpty()) {
			return 0;
		}

		for (int i = 0; i < 3 && i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getPos().equals("VBG")) {
				return 1;
			}
		}
		return 0;
	}

	private List<Integer> findMainTokens(List<Token> tokens) {

		List<Integer> elements = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB") && token.getLemma().equals("trigger")) {
				elements.add(i);
			}
		}
		return elements;
	}
}
