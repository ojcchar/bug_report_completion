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

		List<Sentence> sentences = paragraph.getSentences();

		if (sentences.size() < 2) {
			return 0;
		}
		
		
		int numValidSentences = 0;
		int idxLastSentence = -1;
		int numTotalValidSentences = 0;

		// check for at least minNumOfSentences imperative sentences
		for (int i = 0; i < sentences.size(); i++) {

			Sentence sentence = sentences.get(i);
			
			if (sentence.getTokens().stream().anyMatch(tok -> tok.getLemma().equals("should"))) {
				continue;
			}
			
			numTotalValidSentences++;

			// check if any of the clauses is imperative
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			for (Sentence clause : clauses) {
				if (SentenceUtils.isImperativeSentence(clause)) {
					numValidSentences++;
					idxLastSentence = i;
					break;
				}
			}

		}

		
		// check for an OB clause
		if (idxLastSentence!=-1) {
			int idx = SentenceUtils.findObsBehaviorSentence(sentences.subList(idxLastSentence, sentences.size()));
			if (idx != -1) {
				numValidSentences++;
			}
		}else{
			return 0;
		}
		
//		System.out.println(numValidSentences +"-"+numTotalValidSentences);
		
		float matchingSentenceRatio = (float) numValidSentences / numTotalValidSentences;
		
		return matchingSentenceRatio >= 0.5 ? 1 : 0;


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
