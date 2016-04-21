package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class HaveSequencePM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();
		if (sentences.size() == 1) {
			return 0;
		}

		int numSentences = 0;
		for (Sentence sentence : sentences) {
			numSentences += checkSentence(sentence);
		}

		if (numSentences > 1) {
			return 1;
		}

		return 0;
	}

	private int checkSentence(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();

		if (tokens.size() < 3) {
			return 0;
		}

		Token token = tokens.get(0);
		Token token2 = tokens.get(1);
		Token token3 = tokens.get(2);

		if (token.getGeneralPos().equals("PRP") && (token2.getPos().equals("VBP") || token2.getPos().equals("VBZ"))
				&& token2.getLemma().equals("have")) {

			if (token3.getGeneralPos().equals("NN")) {
				return 1;
			} else {
				if (tokens.size() >= 4) {
					Token token4 = tokens.get(4);

					if (token3.getGeneralPos().equals("DT")) {
						if (token4.getGeneralPos().equals("NN")) {
							return 1;
						} else {

							if (tokens.size() >= 5) {
								Token token5 = tokens.get(5);
								if (token4.getGeneralPos().equals("JJ") && token5.getGeneralPos().equals("NN")) {
									return 1;
								}
							}
						}
					}

				}
			}

		}

		return 0;
	}

}
