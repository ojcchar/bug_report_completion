package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ActionsPastPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();
		if (LabeledListPM.isParagraphLabeled(sentences)) {
			return 0;
		}

		int numSentences = 0;
		int bulletedSentences = 0;
		for (int i = 0; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);

			List<Token> tokensNoBullet = LabeledListPM.getTokensNoBullet(sentence);

			// no bullet
			if (tokensNoBullet.isEmpty()) {
				continue;
			}

			bulletedSentences++;
			if (SimplePastParagraphPM
					.countNumClausesInSimplePast(new Sentence(sentence.getId(), tokensNoBullet)) != 0) {
				numSentences++;
			}
		}

		if (bulletedSentences < 2) {
			return 0;
		}

		 System.out.println(numSentences + "-" + bulletedSentences);

		return ((float) numSentences) / bulletedSentences >= 0.5F ? 1 : 0;
	}

}
