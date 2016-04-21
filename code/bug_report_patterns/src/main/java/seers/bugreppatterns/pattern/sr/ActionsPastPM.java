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

	LabeledListPM pm = new LabeledListPM();
	ActionsPresentPM pm2 = new ActionsPresentPM();

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		int mPresent = pm2.matchParagraph(paragraph);
		if (mPresent == 1) {
			return 0;
		}

		List<Sentence> sentences = paragraph.getSentences();
		if (pm.isParagraphLabeled(sentences)) {
			return 0;
		}

		if (ActionsPresentPM.isParagraphLabeled(sentences)) {
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

		List<Token> tokens2 = sentence.getTokens();
		if (ActionsPresentPM.isBullet(tokens2.get(0))) {

			if (tokens2.size() > 2) {
				boolean anAction = isAnAction(tokens2.get(1), tokens2.get(2));
				if (anAction) {
					return 0;
				}
			}

			int i = getFirstPastVerb(tokens2, 1);
			// check the verb is at the beginning of the sentence
			if (i != -1) {
				Token prevToken = tokens2.get(i - 1);
				if ((prevToken.getGeneralPos().equals("PRP") || prevToken.getGeneralPos().equals("NN"))) {
					return 1;
				} else {
					if (i == 1) {
						return 1;
					}
				}
			}

		}
		return 0;

	}

	private boolean isAnAction(Token firstToken, Token token2) {
		if (firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP")) {
			return true;
		}
		return false;
	}

	private int getFirstPastVerb(List<Token> tokens, int j) {
		for (int i = j; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getPos().equals("VBD")) {
				return i;
			}
		}
		return -1;
	}

}
