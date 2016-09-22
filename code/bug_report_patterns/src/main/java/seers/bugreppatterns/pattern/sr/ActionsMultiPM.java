package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
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

		int minNumOfSentences = 1;

		int numImperativeSentences = 0;
		int idxLastSentence = -1;

		// check for at least minNumOfSentences imperative sentences
		List<Sentence> sentences = paragraph.getSentences();
		for (int i = 0; i < sentences.size(); i++) {
			idxLastSentence = i;

			Sentence sentence = sentences.get(i);

			// check if any of the clauses is imperative
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			for (Sentence clause : clauses) {
				if (SentenceUtils.isImperativeSentence(clause)) {
					numImperativeSentences++;
					break;
				}
			}

			if (numImperativeSentences >= minNumOfSentences) {
				break;
			}
		}

		// check for an OB clause
		if (numImperativeSentences >= minNumOfSentences) {
			int idx = SentenceUtils.findObsBehaviorSentence(sentences.subList(idxLastSentence, sentences.size()));
			if (idx != -1) {
				return 1;
			}
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
