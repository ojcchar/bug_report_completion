package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ActionsPresentPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	LabeledListPM pm1 = new LabeledListPM();

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();
		if (pm1.isParagraphLabeled(sentences)) {
			return 0;
		}

		if (isParagraphLabeled(sentences)) {
			return 0;
		}

		int numSentences = 0;
		for (int i = 0; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);
			numSentences += isActionInPresent(sentence, false);
		}

		numSentences += isUsingWould(sentences.get(sentences.size() - 1));

		if (numSentences > 1) {
			return 1;
		}

		return 0;
	}

	private int isUsingWould(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("would")) {
				if (i > 0) {
					Token prevToken = tokens.get(i - 1);
					if (prevToken.getGeneralPos().equals("PRP") || prevToken.getGeneralPos().equals("NN")
							|| prevToken.getGeneralPos().equals("DT")) {
						if (i + 1 < tokens.size()) {
							Token nextToken = tokens.get(i + 1);
							if (nextToken.getGeneralPos().equals("VB")) {
								return 1;
							}
						}
					}
				}

				return 0;

			}
		}
		return 0;
	}

	public static boolean isParagraphLabeled(List<Sentence> sentences) {
		Sentence sentence = sentences.get(0);
		List<Token> tokens = sentence.getTokens();
		Token lastToken = tokens.get(tokens.size() - 1);
		if (tokens.size() <= 5 && lastToken.getLemma().equals(":")) {
			return true;
		}
		return false;
	}

	final static String[] AUX_VERBS = { "do", "be", "have" };

	public static int isActionInPresent(Sentence sentence, boolean noCheckForBullet) {
		List<Token> tokens2 = sentence.getTokens();
		if (noCheckForBullet || isBullet(tokens2.get(0))) {
			int i = getFirstPresentVerb(tokens2, 1);
			// check the verb is at the beginning of the sentence
			if (i != -1) {
				int length = getLengthPreClause(tokens2);
				if (i <= 5 + length) {
					Token verb = tokens2.get(i);
					if (!Arrays.stream(AUX_VERBS).anyMatch(v -> v.equals(verb.getLemma()))) {
						Token prevToken = tokens2.get(i - 1);
						if (prevToken.getGeneralPos().equals("PRP") || prevToken.getGeneralPos().equals("NN")) {
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	private static int getLengthPreClause(List<Token> tokens) {
		for (int i = 0; i < tokens.size() && i <= 5; i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals(",")) {
				return i;
			}
		}
		return 0;
	}

	private static int getFirstPresentVerb(List<Token> tokens, int j) {
		for (int i = j; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getPos().equals("VBP") || token.getPos().equals("VBZ")) {
				return i;
			}
		}
		return -1;
	}

	static boolean isBullet(Token token) {
		String lemma = token.getLemma();
		return lemma.matches("(\\d+|\\-|\\*)");
	}

}
