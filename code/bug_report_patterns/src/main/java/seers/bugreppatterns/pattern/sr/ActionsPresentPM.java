package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ActionsPresentPM extends StepsToReproducePatternMatcher {

	public final static Set<String> POS = JavaUtils.getSet("VBP", "VBZ");
	public final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet("do", "be", "have", "seem");

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
			if (isSimplePresentSentence(new Sentence("0", tokensNoBullet))) {
				numSentences++;
			}
		}

		numSentences += isUsingWould(sentences.get(sentences.size() - 1));

		return ((float) numSentences) / bulletedSentences > 0.5F ? 1 : 0;
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

	public static boolean isSimplePresentSentence(Sentence sentence) {
		SimpleTenseChecker checker = new SimpleTenseChecker(POS, null, EXCLUDED_VERBS);
		return checker.countNumClauses(sentence) != 0;
	}

}
