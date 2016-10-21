package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LabeledParagraphPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();

		// LabeledListPM pm = new LabeledListPM();
		// if (pm.matchParagraph(paragraph) != 0) {
		// return 0;
		// }

		int labelIdx = LabeledListPM.getLabelIndex(sentences);
		int match = 0;
		if (labelIdx != -1) {
			match = checkForNonBulletSentences(sentences, labelIdx);
		} else {
			labelIdx = getUncommonLabelIndex(sentences);
			if (labelIdx != -1) {
				match = checkForNonBulletSentences(sentences, labelIdx);
			}
		}

		return match;
	}

	private int getUncommonLabelIndex(List<Sentence> sentences) {
		for (int i = 0; i < sentences.size(); i++) {
			String text = TextProcessor.getStringFromLemmas(sentences.get(i));
			boolean b = text.matches("(?s)method \\d :.*");
			if (b) {
				return i;
			}
		}
		return -1;
	}

	private int checkForNonBulletSentences(List<Sentence> sentences, int labelIdx) {
		int match = 0;

		// cases such: "Steps to reproduce: Just editing any..."
		if (labelIdx + 1 == sentences.size()) {

			Sentence sentence = sentences.get(labelIdx);
			List<Token> tokens = sentence.getTokens();

			for (int i = 0; i < tokens.size(); i++) {
				Token token = tokens.get(i);

				// find the ':' token
				if (token.getLemma().equals(":")) {
					if (i + 1 != tokens.size()) {

						// check for bullets after the ':'
						Sentence sentence2 = new Sentence("0", tokens.subList(i + 1, tokens.size()));
						List<Token> tokensNoBullet = LabeledListPM.getTokensNoBullet(sentence2);
						if (tokensNoBullet.isEmpty()) {
							match = 1;
						}
					}

					break;

				}
			}

		} else {
			
			boolean thereAreBullets = false;

			// regular cases, when there are multiple sentences after the label
			for (int i = labelIdx + 1; i < sentences.size(); i++) {
				Sentence sentence = sentences.get(i);
				List<Token> tokensNoBullet = LabeledListPM.getTokensNoBullet(sentence);
				
				//this means there are bullets
				if (!tokensNoBullet.isEmpty()) {
					thereAreBullets = true;
					break;
				}
			}
			
			if (!thereAreBullets) {
				match =1;
			}
		}

		return match;
	}

}
