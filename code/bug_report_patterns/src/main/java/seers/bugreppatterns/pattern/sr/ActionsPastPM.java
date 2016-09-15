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
		for (int i = 0; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);
			numSentences += isActionInPast(sentence);
		}

		if (numSentences > 1) {
			return 1;
		}

		return 0;
	}

	private int isActionInPast(Sentence sentence) {

		List<Token> tokensNoBullet = LabeledListPM.getTokensNoBullet(sentence);

		// no bullet
		if (tokensNoBullet.isEmpty()) {
			return 0;
		}

		if (SimplePastParagraphPM.countNumClausesInSimplePresent(new Sentence(sentence.getId(), tokensNoBullet)) != 0) {
			return 1;
		}
		return 0;
	}

}
