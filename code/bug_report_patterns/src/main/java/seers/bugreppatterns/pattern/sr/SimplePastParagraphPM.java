package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SimplePastParagraphPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		int num = 0;

		List<Sentence> sentence2 = paragraph.getSentences();
		for (Sentence sentence : sentence2) {
			List<Token> tokens = sentence.getTokens();
			List<Integer> verbs = findMainTerms(tokens);

			for (Integer verb : verbs) {
				Token token = tokens.get(verb);

				// case: I performed
				if (token.getPos().equals("VBD") || token.getPos().equals("VBN") || token.getLemma().equals("set")) {
					if (verb - 1 >= 0) {
						Token prevToken = tokens.get(verb - 1);
						if (prevToken.getLemma().equals("i") || prevToken.getGeneralPos().equals("PRP")
								|| prevToken.getGeneralPos().equals("CC") || prevToken.getLemma().equals(",")
								|| prevToken.getLemma().equals("_")) {
							num++;
						}
					}

					// case: I have performed
				} else if ((token.getPos().equals("VBP") || token.getPos().equals("VBZ"))
						&& (token.getLemma().equals("have") || token.getLemma().equals("ve"))) {
					if (verb - 1 >= 0) {
						Token prevToken = tokens.get(verb - 1);
						if (verb + 1 < tokens.size()) {
							Token nextToken = tokens.get(verb + 1);
							if (prevToken.getLemma().equals("i") || prevToken.getGeneralPos().equals("PRP")) {
								if (nextToken.getPos().equals("VBN") || nextToken.getPos().equals("VB")) {
									num++;
								}
							}
						}
					}
				}

			}

		}
		if (num > 1) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findMainTerms(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				idxs.add(i);
			}
		}
		return idxs;
	}
}
