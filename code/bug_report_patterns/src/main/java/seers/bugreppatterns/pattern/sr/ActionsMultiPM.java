package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.eb.ImperativeSentencePM;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ActionsMultiPM extends StepsToReproducePatternMatcher {
	ImperativeSentencePM pm = new ImperativeSentencePM();

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		int num = 0;
		List<Sentence> sentences = paragraph.getSentences();
		for (Sentence sentence : sentences) {
			List<Token> tokens = sentence.getTokens();

			Token firstToken = tokens.get(0);
			if (tokens.size() < 2) {
				continue;
			}
			Token secondToken = tokens.get(1);
			int match = checkNormalCase(firstToken, secondToken);
			if (match == 1) {
				num++;
			} else {
				for (int i = 0; i < tokens.size(); i++) {
					Token tok = tokens.get(i);
					if (tok.getLemma().matches("\\p{Punct}")) {
						if (i + 1 < tokens.size()) {
							Token firstToken2 = tokens.get(i + 1);
							match = checkNormalCase(firstToken2, null);
							if (match == 1) {
								num++;
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

	final private static String[] UNDETECTED_VERBS = { "show", "boomark", "rename", "run", "select", "post", "stop" };

	public int checkNormalCase(Token firstToken, Token secondToken) {

		if (firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP")) {
			return 1;
		} else {
			if (secondToken != null) {

				if (firstToken.getPos().equals("RB")
						&& (secondToken.getPos().equals("VB") || secondToken.getPos().equals("VBP"))) {
					return 1;
				}
			}
			if (Arrays.stream(UNDETECTED_VERBS).anyMatch(p -> firstToken.getLemma().equals(p))) {
				return 1;
			}
		}
		return 0;
	}

}
