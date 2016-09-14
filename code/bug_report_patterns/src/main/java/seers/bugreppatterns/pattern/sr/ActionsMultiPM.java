package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.eb.ImperativeSentencePM;
import seers.bugreppatterns.utils.SentenceUtils;
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
		int idxLastSentence = -1;
		List<Sentence> sentences = paragraph.getSentences();
		for (int i = 0; i < sentences.size(); i++) {
			idxLastSentence = i;

			Sentence sentence = sentences.get(i);
			List<Token> tokens = sentence.getTokens();
			List<List<Token>> clauses = SentenceUtils.extractClauses(tokens);
			for (List<Token> clause : clauses) {
				if (ImperativeSubordinatesPM.isImperative(clause) == 1) {
					num++;
					break;
				}
			}

			if (num > 1) {
				break;
			}
		}

		if (num > 1) {
			int idx = findOBClause(sentences.subList(idxLastSentence, sentences.size()),
					ConditionalAffirmativePM.OB_PMS);
			if (idx != -1) {
				return 1;
			}
		}

		return 0;

	}

	private static int findOBClause(List<Sentence> sentences, ObservedBehaviorPatternMatcher[] patterns)
			throws Exception {
		for (int i = sentences.size() - 1; i >= 0; i--) {
			Sentence sentence = sentences.get(i);

			for (ObservedBehaviorPatternMatcher pm : patterns) {
				if (pm.matchSentence(sentence) == 1) {
					return i;
				}
			}
		}
		return -1;
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
