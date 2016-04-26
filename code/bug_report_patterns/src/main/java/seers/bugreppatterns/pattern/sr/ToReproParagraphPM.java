package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.eb.ImperativeSentencePM;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ToReproParagraphPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();

		if (sentences.size() > 1) {
			Sentence sentence = sentences.get(0);
			List<Token> tokens = sentence.getTokens();

			boolean repro = false;
			for (int i = 0; i < 4 && i < tokens.size(); i++) {
				if (tokens.get(i).getLemma().equals("to")) {
					if (i + 1 < tokens.size()) {
						if (tokens.get(i + 1).getLemma().equals("repro")
								|| tokens.get(i + 1).getLemma().equals("reproduce")) {

							if (i + 2 < tokens.size()) {
								if (!tokens.get(i + 2).getLemma().equals(":")) {
									repro = true;
								}
							}
							break;
						}
					}
				}
			}

			if (repro) {
				int numActions = checkForActions(sentences.subList(1, sentences.size()));
				if (numActions > 0) {
					return 1;
				}
			}
		}

		return 0;
	}

	private int checkForActions(List<Sentence> sentences) {

		int num = 0;
		for (Sentence sentence : sentences) {
			List<Token> tokens = sentence.getTokens();
			if (tokens.size() > 1) {
				num += ImperativeSentencePM.checkNormalCase(tokens.get(0), tokens.get(1));
			}
		}
		return num;
	}

}
